package com.lms.leaveservice.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lms.leaveservice.client.EmployeeClient;
import com.lms.leaveservice.dto.DeductLeaveRequest;
import com.lms.leaveservice.dto.EmployeeBalanceResponse;
import com.lms.leaveservice.dto.LeaveApplyRequest;
import com.lms.leaveservice.dto.NotificationMessage;
import com.lms.leaveservice.dto.RejectRequest;
import com.lms.leaveservice.entity.LeaveRequest;
import com.lms.leaveservice.entity.LeaveStatus;
import com.lms.leaveservice.repository.LeaveRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LeaveService {

    private final EmployeeClient employeeClient;
    private final LeaveRepository repository;
    private final NotificationProducer notificationProducer;

    @CircuitBreaker(name = "employeeService", fallbackMethod = "applyLeaveFallback")
    public LeaveRequest applyLeave(LeaveApplyRequest request) {

        validateDates(request);
        validateBalance(request);
        // Check overlapping leaves
        validateOverlap(request);
        // validateDates(request);

        LeaveRequest leave = LeaveRequest.builder().employeeId(request.getEmployeeId()).managerId(request
            .getManagerId()).leaveType(request.getLeaveType()).startDate(request.getStartDate()).endDate(request
                .getEndDate()).days(request.getDays()).reason(request.getReason()).status(LeaveStatus.PENDING).build();

        LeaveRequest saved = repository.save(leave);

        notificationProducer.send(new NotificationMessage("LEAVE_APPLIED", "Employee " + saved.getEmployeeId()
            + " applied for " + saved.getDays() + " day(s)"));

        return saved;

        // return repository.save(leave);

    }

    private void validateDates(LeaveApplyRequest request) {

        if (request.getStartDate().isBefore(LocalDate.now())) {

            throw new RuntimeException("Past dates not allowed");
        }

        if (request.getEndDate().isBefore(request.getStartDate())) {

            throw new RuntimeException("Invalid Date Range");
        }
    }

    private void validateBalance(LeaveApplyRequest request) {

        EmployeeBalanceResponse balance = employeeClient.getBalance(request.getEmployeeId());

        switch (request.getLeaveType().toUpperCase()) {

            case "CASUAL":

                if (balance.getCasualLeave() < request.getDays()) {

                    throw new RuntimeException("Insufficient Casual Leave");
                }

                break;

            case "SICK":

                if (balance.getSickLeave() < request.getDays()) {

                    throw new RuntimeException("Insufficient Sick Leave");
                }

                break;

            case "PRIVILEGE":

                if (balance.getPrivilegeLeave() < request.getDays()) {

                    throw new RuntimeException("Insufficient Privilege Leave");
                }

                break;
        }
    }

    public List<LeaveRequest> getHistory(Long employeeId) {

        return repository.findByEmployeeId(employeeId);
    }

    public List<LeaveRequest> pendingRequests(Long managerId) {

        return repository.findByManagerIdAndStatus(managerId, LeaveStatus.PENDING);
    }

    private void validateOverlap(LeaveApplyRequest request) {

        List<LeaveRequest> leaves = repository.findByEmployeeId(request.getEmployeeId());

        for (LeaveRequest leave : leaves) {

            if (leave.getStatus() == LeaveStatus.REJECTED) {

                continue;
            }

            boolean overlap = !(request.getEndDate().isBefore(leave.getStartDate()) || request.getStartDate().isAfter(
                leave.getEndDate()));

            if (overlap) {

                throw new RuntimeException("Overlapping leave exists");
            }
        }
    }

    public LeaveRequest approveLeave(Long leaveId) {

        LeaveRequest leave = repository.findById(leaveId).orElseThrow(() -> new RuntimeException(
            "Leave Request Not Found"));

        if (leave.getStatus() != LeaveStatus.PENDING) {

            throw new RuntimeException("Only Pending Leaves Can Be Approved");
        }

        DeductLeaveRequest deductRequest = new DeductLeaveRequest();

        deductRequest.setLeaveType(leave.getLeaveType());

        deductRequest.setDays(leave.getDays());

        employeeClient.deductLeave(leave.getEmployeeId(), deductRequest);

        leave.setStatus(LeaveStatus.APPROVED);

        notificationProducer.send(new NotificationMessage("LEAVE_APPROVED", "Leave " + leave.getId() + " approved"));

        return repository.save(leave);
    }

    public LeaveRequest rejectLeave(Long leaveId, RejectRequest request) {

        LeaveRequest leave = repository.findById(leaveId).orElseThrow(() -> new RuntimeException(
            "Leave Request Not Found"));

        if (leave.getStatus() != LeaveStatus.PENDING) {

            throw new RuntimeException("Only Pending Leaves Can Be Rejected");
        }

        leave.setStatus(LeaveStatus.REJECTED);

        leave.setRejectionReason(request.getReason());

        notificationProducer.send(new NotificationMessage("LEAVE_REJECTED", "Leave " + leave.getId() + " rejected"));

        return repository.save(leave);
    }

    public List<LeaveRequest> historyByStatus(Long employeeId, LeaveStatus status) {

        return repository.findByEmployeeIdAndStatus(employeeId, status);
    }

    public LeaveRequest applyLeaveFallback(LeaveApplyRequest request, Exception ex) {

        throw new RuntimeException("Employee Service is currently unavailable. Please try again later.");
    }

}

package com.lms.employeeservice.service;

import org.springframework.stereotype.Service;

import com.lms.employeeservice.dto.DeductLeaveRequest;
import com.lms.employeeservice.dto.EmployeeRequest;
import com.lms.employeeservice.dto.LeaveBalanceResponse;
import com.lms.employeeservice.entity.Employee;
import com.lms.employeeservice.entity.LeaveBalance;
import com.lms.employeeservice.repository.EmployeeRepository;
import com.lms.employeeservice.repository.LeaveBalanceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final LeaveBalanceRepository leaveBalanceRepository;

    public Employee createEmployee(EmployeeRequest request) {

        Employee employee = Employee.builder().employeeCode(request.getEmployeeCode()).name(request.getName()).email(
            request.getEmail()).managerId(request.getManagerId()).build();

        Employee saved = employeeRepository.save(employee);

        LeaveBalance balance = LeaveBalance.builder().employeeId(saved.getId()).casualLeave(12).sickLeave(10)
            .privilegeLeave(15).build();

        leaveBalanceRepository.save(balance);

        return saved;
    }

    public Employee getEmployee(Long id) {

        return employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    public LeaveBalanceResponse getLeaveBalance(Long employeeId) {

        LeaveBalance balance = leaveBalanceRepository.findByEmployeeId(employeeId).orElseThrow(
            () -> new RuntimeException("Leave balance not found"));

        return LeaveBalanceResponse.builder().casualLeave(balance.getCasualLeave()).sickLeave(balance.getSickLeave())
            .privilegeLeave(balance.getPrivilegeLeave()).build();
    }

    public String deductLeave(Long employeeId, DeductLeaveRequest request) {

        LeaveBalance balance = leaveBalanceRepository.findByEmployeeId(employeeId).orElseThrow(
            () -> new RuntimeException("Balance not found"));

        switch (request.getLeaveType().toUpperCase()) {

            case "CASUAL":

                if (balance.getCasualLeave() < request.getDays()) {

                    throw new RuntimeException("Insufficient Leave");
                }

                balance.setCasualLeave(balance.getCasualLeave() - request.getDays());

                break;

            case "SICK":

                if (balance.getSickLeave() < request.getDays()) {

                    throw new RuntimeException("Insufficient Leave");
                }

                balance.setSickLeave(balance.getSickLeave() - request.getDays());

                break;

            case "PRIVILEGE":

                if (balance.getPrivilegeLeave() < request.getDays()) {

                    throw new RuntimeException("Insufficient Leave");
                }

                balance.setPrivilegeLeave(balance.getPrivilegeLeave() - request.getDays());

                break;

            default:

                throw new RuntimeException("Invalid Leave Type");
        }

        leaveBalanceRepository.save(balance);

        return "Leave Deducted";
    }

}
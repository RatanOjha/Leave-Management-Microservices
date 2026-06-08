
package com.lms.leaveservice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lms.leaveservice.dto.LeaveApplyRequest;
import com.lms.leaveservice.dto.RejectRequest;
import com.lms.leaveservice.entity.LeaveRequest;
import com.lms.leaveservice.entity.LeaveStatus;
import com.lms.leaveservice.service.LeaveService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/leave")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveService leaveService;

    @PostMapping("/apply")
    public LeaveRequest applyLeave(@RequestBody LeaveApplyRequest request) {

        return leaveService.applyLeave(request);
    }

    @GetMapping("/history/{employeeId}")
    public List<LeaveRequest> history(@PathVariable Long employeeId) {

        return leaveService.getHistory(employeeId);
    }

    @GetMapping("/manager/{managerId}/pending")
    public List<LeaveRequest> pending(@PathVariable Long managerId) {

        return leaveService.pendingRequests(managerId);
    }

    @PutMapping("/approve/{leaveId}")
    public LeaveRequest approveLeave(@PathVariable Long leaveId) {

        return leaveService.approveLeave(leaveId);
    }

    @PutMapping("/reject/{leaveId}")
    public LeaveRequest rejectLeave(

        @PathVariable Long leaveId,

        @RequestBody RejectRequest request) {

        return leaveService.rejectLeave(leaveId, request);
    }

    @GetMapping("/history/{employeeId}/{status}")
    public List<LeaveRequest> historyByStatus(

        @PathVariable Long employeeId,

        @PathVariable LeaveStatus status) {

        return leaveService.historyByStatus(employeeId, status);
    }

}
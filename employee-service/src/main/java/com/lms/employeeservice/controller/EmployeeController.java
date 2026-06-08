
package com.lms.employeeservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lms.employeeservice.dto.DeductLeaveRequest;
import com.lms.employeeservice.dto.EmployeeRequest;
import com.lms.employeeservice.dto.LeaveBalanceResponse;
import com.lms.employeeservice.entity.Employee;
import com.lms.employeeservice.service.EmployeeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public Employee createEmployee(@RequestBody EmployeeRequest request) {

        return employeeService.createEmployee(request);
    }

    @GetMapping("/{id}")
    public Employee getEmployee(@PathVariable Long id) {

        return employeeService.getEmployee(id);
    }

    @GetMapping("/{id}/balance")
    public LeaveBalanceResponse getBalance(@PathVariable Long id) {

        return employeeService.getLeaveBalance(id);
    }

    @PutMapping("/{id}/deduct")
    public String deductLeave(@PathVariable Long id,

        @RequestBody DeductLeaveRequest request) {

        return employeeService.deductLeave(id, request);
    }

}
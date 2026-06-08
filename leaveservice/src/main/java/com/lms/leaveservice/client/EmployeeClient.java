
package com.lms.leaveservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.lms.leaveservice.dto.DeductLeaveRequest;
import com.lms.leaveservice.dto.EmployeeBalanceResponse;

@FeignClient(name = "EMPLOYEE-SERVICE")
public interface EmployeeClient {

    @GetMapping("/employees/{id}/balance")
    EmployeeBalanceResponse getBalance(@PathVariable Long id);

    @PutMapping("/employees/{id}/deduct")
    String deductLeave(@PathVariable Long id, @RequestBody DeductLeaveRequest request);
}
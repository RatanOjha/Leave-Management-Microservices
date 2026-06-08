
package com.lms.employeeservice.dto;

import lombok.Data;

@Data
public class DeductLeaveRequest {

    private String leaveType;

    private Integer days;
}
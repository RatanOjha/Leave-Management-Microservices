
package com.lms.leaveservice.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class LeaveApplyRequest {

    private Long employeeId;

    private Long managerId;

    private String leaveType;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer days;

    private String reason;
}
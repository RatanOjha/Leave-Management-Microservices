
package com.lms.leaveservice.dto;

import lombok.Data;

@Data
public class DeductLeaveRequest {

    private String leaveType;

    private Integer days;
}
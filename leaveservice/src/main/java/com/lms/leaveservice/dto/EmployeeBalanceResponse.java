
package com.lms.leaveservice.dto;

import lombok.Data;

@Data
public class EmployeeBalanceResponse {

    private Integer casualLeave;

    private Integer sickLeave;

    private Integer privilegeLeave;
}
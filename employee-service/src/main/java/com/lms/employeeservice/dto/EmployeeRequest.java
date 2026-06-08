
package com.lms.employeeservice.dto;

import lombok.Data;

@Data
public class EmployeeRequest {

    private String employeeCode;

    private String name;

    private String email;

    private Long managerId;
}
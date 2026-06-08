
package com.lms.employeeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.employeeservice.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
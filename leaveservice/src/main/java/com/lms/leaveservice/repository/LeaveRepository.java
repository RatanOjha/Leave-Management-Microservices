
package com.lms.leaveservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.leaveservice.entity.LeaveRequest;
import com.lms.leaveservice.entity.LeaveStatus;

public interface LeaveRepository extends JpaRepository<LeaveRequest, Long> {

    List<LeaveRequest> findByEmployeeId(Long employeeId);

    List<LeaveRequest> findByManagerIdAndStatus(Long managerId, LeaveStatus status);

    List<LeaveRequest> findByEmployeeIdAndStatus(Long employeeId, LeaveStatus status);

}

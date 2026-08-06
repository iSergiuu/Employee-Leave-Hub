package com.leavehub.backend.repository;

import com.leavehub.backend.entity.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findTop5ByEmployeeIdOrderByCreatedAtDesc(Long employeeId);
    int countByEmployeeIdAndStatus(Long employeeId, String status);
}
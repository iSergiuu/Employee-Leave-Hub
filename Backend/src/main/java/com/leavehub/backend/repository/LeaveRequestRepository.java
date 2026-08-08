package com.leavehub.backend.repository;

import com.leavehub.backend.entity.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    List<LeaveRequest> findTop5ByEmployeeIdOrderByCreatedAtDesc(Long employeeId);

    int countByEmployeeIdAndStatus(Long employeeId, String status);

    List<LeaveRequest> findByEmployeeDepartmentIdOrderByCreatedAtDesc(Long departmentId);

    @Query("SELECT r FROM LeaveRequest r WHERE r.employee.department.id = :departmentId AND r.status = 'APPROVED' AND (r.startDate <= :endDate AND r.endDate >= :startDate)")
    List<LeaveRequest> findApprovedByDepartmentAndDateRange(
            @Param("departmentId") Long departmentId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    List<LeaveRequest> findByEmployeeEmailOrderByCreatedAtDesc(String email);
}
package com.leavehub.backend.repository;

import com.leavehub.backend.entity.LeaveWorkflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveWorkflowRepository extends JpaRepository<LeaveWorkflow, Long> {
}
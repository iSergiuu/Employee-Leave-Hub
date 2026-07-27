package com.leavehub.backend.repository;

import com.leavehub.backend.entity.LeaveBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {
}
package com.leavehub.backend.service;

import com.leavehub.backend.entity.LeaveBalance;
import com.leavehub.backend.repository.LeaveBalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LeaveBalanceService {

    private final LeaveBalanceRepository leaveBalanceRepository;

    public LeaveBalance getBalance(Long employeeId, Long leaveTypeId, Integer year) {
        return leaveBalanceRepository.findByEmployeeIdAndLeaveTypeIdAndYear(employeeId, leaveTypeId, year)
                .orElseThrow(() -> new RuntimeException(
                        "Nu s-a găsit o balanță de concediu pentru angajatul " + employeeId + " în anul " + year));
    }

    public boolean hasSufficientDays(Long employeeId, Long leaveTypeId, Integer year, Integer requestedDays) {
        LeaveBalance balance = getBalance(employeeId, leaveTypeId, year);

        int availableDays = balance.getTotalAllocated() - balance.getDaysUsed();
        return availableDays >= requestedDays;
    }

    public void consumeDays(Long employeeId, Long leaveTypeId, Integer year, Integer daysToConsume) {
        LeaveBalance balance = getBalance(employeeId, leaveTypeId, year);

        balance.setDaysUsed(balance.getDaysUsed() + daysToConsume);

        leaveBalanceRepository.save(balance);
    }
}
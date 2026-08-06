package com.leavehub.backend.service;

import com.leavehub.backend.dto.EmployeeDashboardDTO;
import com.leavehub.backend.entity.Employee;
import com.leavehub.backend.entity.LeaveBalance;
import com.leavehub.backend.entity.LeaveRequest;
import com.leavehub.backend.repository.EmployeeRepository;
import com.leavehub.backend.repository.LeaveBalanceRepository;
import com.leavehub.backend.repository.LeaveRequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final EmployeeRepository employeeRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;
    private final LeaveRequestRepository leaveRequestRepository;

    public DashboardService(EmployeeRepository employeeRepository,
                            LeaveBalanceRepository leaveBalanceRepository,
                            LeaveRequestRepository leaveRequestRepository) {
        this.employeeRepository = employeeRepository;
        this.leaveBalanceRepository = leaveBalanceRepository;
        this.leaveRequestRepository = leaveRequestRepository;
    }

    public EmployeeDashboardDTO getDashboardData(String email) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Angajatul nu a fost gasit"));

        int currentYear = LocalDate.now().getYear();

        List<LeaveBalance> balances = leaveBalanceRepository.findAll().stream()
                .filter(b -> b.getEmployee().getId().equals(employee.getId()) && b.getYear().equals(currentYear))
                .collect(Collectors.toList());

        int totalAvailable = 0;
        int totalUsed = 0;
        for (LeaveBalance b : balances) {
            if ("CO".equals(b.getLeaveType().getCode())) {
                totalAvailable += (b.getTotalAllocated() - b.getDaysUsed());
                totalUsed += b.getDaysUsed();
            }
        }

        int pendingCount = leaveRequestRepository.countByEmployeeIdAndStatus(employee.getId(), "PENDING");
        int approvedCount = leaveRequestRepository.countByEmployeeIdAndStatus(employee.getId(), "APPROVED");

        EmployeeDashboardDTO.StatsDTO stats = new EmployeeDashboardDTO.StatsDTO(
                totalAvailable, totalUsed, pendingCount, approvedCount
        );

        List<LeaveRequest> recentRequestsList = leaveRequestRepository.findTop5ByEmployeeIdOrderByCreatedAtDesc(employee.getId());

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", new Locale("ro", "RO"));
        DateTimeFormatter dayMonthFormatter = DateTimeFormatter.ofPattern("dd MMM", new Locale("ro", "RO"));

        List<EmployeeDashboardDTO.RequestDTO> requests = recentRequestsList.stream().map(req -> {
            String typeName = req.getLeaveType().getName() + " (" + req.getLeaveType().getCode() + ")";
            String period = req.getStartDate().format(dayMonthFormatter) + " - " + req.getEndDate().format(dateFormatter);
            String statusRO = translateStatus(req.getStatus());
            String actionDate = req.getCreatedAt() != null ? req.getCreatedAt().toLocalDate().format(dateFormatter) : LocalDate.now().format(dateFormatter);

            // AICI ESTE MODIFICAREA: Trimitem req.getId() ca prim parametru
            return new EmployeeDashboardDTO.RequestDTO(req.getId(), typeName, period, req.getWorkingDays(), statusRO, actionDate);
        }).collect(Collectors.toList());

        List<EmployeeDashboardDTO.BalanceDTO> balanceDTOs = balances.stream().map(b -> {
            String typeName = b.getLeaveType().getName() + " (" + b.getLeaveType().getCode() + ")";
            String colorClass = "CO".equals(b.getLeaveType().getCode()) ? "progress-blue" : "progress-green";

            return new EmployeeDashboardDTO.BalanceDTO(typeName, b.getDaysUsed(), b.getTotalAllocated(), colorClass);
        }).collect(Collectors.toList());

        return new EmployeeDashboardDTO(stats, requests, balanceDTOs);
    }

    private String translateStatus(String status) {
        return switch (status) {
            case "PENDING" -> "În Așteptare";
            case "APPROVED" -> "Aprobat";
            case "REJECTED" -> "Respins";
            case "DRAFT" -> "Ciornă";
            case "CANCELLED" -> "Anulat";
            default -> status;
        };
    }
}
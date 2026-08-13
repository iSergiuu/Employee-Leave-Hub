package com.leavehub.backend.service;

import com.leavehub.backend.dto.EmployeeDashboardDTO;
import com.leavehub.backend.dto.LeaveRequestHistoryDTO;
import com.leavehub.backend.entity.Employee;
import com.leavehub.backend.entity.LeaveBalance;
import com.leavehub.backend.entity.LeaveRequest;
import com.leavehub.backend.repository.EmployeeRepository;
import com.leavehub.backend.repository.LeaveBalanceRepository;
import com.leavehub.backend.repository.LeaveRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        // 1. Preluăm toate cererile APROBATE din anul curent pentru a calcula dinamic zilele
        List<LeaveRequest> currentYearApprovedRequests = leaveRequestRepository.findByEmployeeEmailOrderByCreatedAtDesc(email).stream()
                .filter(req -> "APPROVED".equalsIgnoreCase(req.getStatus()))
                .filter(req -> req.getStartDate() != null && req.getStartDate().getYear() == currentYear)
                .collect(Collectors.toList());

        List<LeaveBalance> balances = leaveBalanceRepository.findAll().stream()
                .filter(b -> b.getEmployee().getId().equals(employee.getId()) && b.getYear().equals(currentYear))
                .collect(Collectors.toList());

        int totalAvailable = 0;
        int totalUsed = 0;
        List<EmployeeDashboardDTO.BalanceDTO> balanceDTOs = new java.util.ArrayList<>();

        // 2. Parcurgem balanța, dar folosim calculele noastre reale în loc de valorile statice din DB
        for (LeaveBalance b : balances) {

            // Calculăm zilele folosite real pentru tipul curent de concediu (ex: doar CO sau doar CM)
            int realUsedDays = currentYearApprovedRequests.stream()
                    .filter(req -> req.getLeaveType() != null && req.getLeaveType().getId().equals(b.getLeaveType().getId()))
                    .mapToInt(req -> req.getWorkingDays() != null ? req.getWorkingDays() : 0)
                    .sum();

            if ("CO".equals(b.getLeaveType().getCode())) {
                totalAvailable += (b.getTotalAllocated() - realUsedDays);
                totalUsed += realUsedDays;
            }

            String typeName = b.getLeaveType().getName() + " (" + b.getLeaveType().getCode() + ")";
            String colorClass = "CO".equals(b.getLeaveType().getCode()) ? "progress-blue" : "progress-green";

            // Trimitem 'realUsedDays' către Angular
            balanceDTOs.add(new EmployeeDashboardDTO.BalanceDTO(typeName, realUsedDays, b.getTotalAllocated(), colorClass));
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
            String typeName = (req.getLeaveType() != null) ? req.getLeaveType().getName() + " (" + req.getLeaveType().getCode() + ")" : "Concediu";
            String period = req.getStartDate().format(dayMonthFormatter) + " - " + req.getEndDate().format(dateFormatter);
            String statusRO = translateStatus(req.getStatus());
            String actionDate = req.getCreatedAt() != null ? req.getCreatedAt().toLocalDate().format(dateFormatter) : LocalDate.now().format(dateFormatter);

            return new EmployeeDashboardDTO.RequestDTO(req.getId(), typeName, period, req.getWorkingDays(), statusRO, actionDate);
        }).collect(Collectors.toList());

        return new EmployeeDashboardDTO(stats, requests, balanceDTOs);
    }

    @Transactional(readOnly = true)
    public List<LeaveRequestHistoryDTO> getEmployeeLeaveHistory(String email) {
        return leaveRequestRepository.findByEmployeeEmailOrderByCreatedAtDesc(email).stream().map(req -> {
            Long typeId = (req.getLeaveType() != null) ? req.getLeaveType().getId() : 0L;
            String typeName = (req.getLeaveType() != null) ? req.getLeaveType().getName() : "Concediu";

            LeaveRequestHistoryDTO.LeaveTypeDTO typeDTO = new LeaveRequestHistoryDTO.LeaveTypeDTO(typeId, typeName);

            String startDate = (req.getStartDate() != null) ? req.getStartDate().toString() : "";
            String endDate = (req.getEndDate() != null) ? req.getEndDate().toString() : "";
            String actionDate = (req.getCreatedAt() != null) ? req.getCreatedAt().toLocalDate().toString() : LocalDate.now().toString();
            String status = (req.getStatus() != null) ? req.getStatus() : "PENDING";
            int days = (req.getWorkingDays() != null) ? req.getWorkingDays() : 0;

            return new LeaveRequestHistoryDTO(
                    req.getId(),
                    typeDTO,
                    startDate,
                    endDate,
                    days,
                    status,
                    actionDate
            );
        }).collect(Collectors.toList());
    }

    private String translateStatus(String status) {
        if (status == null) return "";
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
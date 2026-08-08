package com.leavehub.backend.controller;

import com.leavehub.backend.dto.EmployeeDashboardDTO;
import com.leavehub.backend.dto.LeaveRequestHistoryDTO;
import com.leavehub.backend.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/employee/{email}")
    public ResponseEntity<EmployeeDashboardDTO> getEmployeeDashboard(@PathVariable String email) {
        EmployeeDashboardDTO dashboardData = dashboardService.getDashboardData(email);
        return ResponseEntity.ok(dashboardData);
    }

    @GetMapping("/employee/{email}/history")
    public ResponseEntity<List<LeaveRequestHistoryDTO>> getEmployeeHistory(@PathVariable String email) {
        List<LeaveRequestHistoryDTO> history = dashboardService.getEmployeeLeaveHistory(email);
        return ResponseEntity.ok(history);
    }
}
package com.leavehub.backend.controller;

import com.leavehub.backend.dto.EmployeeDashboardDTO;
import com.leavehub.backend.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
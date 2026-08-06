package com.leavehub.backend.controller;

import com.leavehub.backend.dto.AdminDTO;
import com.leavehub.backend.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/employees")
    public ResponseEntity<List<AdminDTO.EmployeeInfo>> getEmployees() {
        return ResponseEntity.ok(adminService.getAllEmployees());
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<Void> updateEmployee(@PathVariable Long id, @RequestBody AdminDTO.EmployeeInfo dto) {
        adminService.updateEmployeeRoleAndDept(id, dto.getRole(), dto.getDeptId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        try {
            adminService.deleteEmployee(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Nu se poate sterge. Acest cont are cereri sau balante asociate in sistem.");
        }
    }

    @GetMapping("/departments")
    public ResponseEntity<List<AdminDTO.DepartmentInfo>> getDepartments() {
        return ResponseEntity.ok(adminService.getAllDepartments());
    }

    @PostMapping("/departments")
    public ResponseEntity<Void> createDepartment(@RequestBody AdminDTO.DepartmentInfo dto) {
        adminService.createDepartment(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/departments/{id}")
    public ResponseEntity<?> deleteDepartment(@PathVariable Long id) {
        try {
            adminService.deleteDepartment(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Nu se poate sterge. Exista angajati asociati acestui departament.");
        }
    }

    @GetMapping("/holidays")
    public ResponseEntity<List<AdminDTO.HolidayInfo>> getHolidays() {
        return ResponseEntity.ok(adminService.getAllHolidays());
    }

    @PostMapping("/holidays")
    public ResponseEntity<Void> createHoliday(@RequestBody AdminDTO.HolidayInfo dto) {
        adminService.createHoliday(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/holidays/{id}")
    public ResponseEntity<?> deleteHoliday(@PathVariable Long id) {
        try {
            adminService.deleteHoliday(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Eroare la stergerea sarbatorii.");
        }
    }
    @GetMapping("/timeline")
    public ResponseEntity<List<AdminDTO.TimelineEventInfo>> getTimeline() {
        return ResponseEntity.ok(adminService.getWorkflowTimeline());
    }

    @GetMapping("/reports/{reportType}")
    public ResponseEntity<byte[]> downloadReport(@PathVariable String reportType) {
        try {
            byte[] pdfBytes = adminService.generateAdminReport(reportType);

            return ResponseEntity.ok()
                    .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Raport_" + reportType + ".pdf")
                    .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
package com.leavehub.backend.controller;

import com.leavehub.backend.entity.LeaveRequest;
import com.leavehub.backend.entity.LeaveType;
import com.leavehub.backend.repository.LeaveTypeRepository;
import com.leavehub.backend.service.LeaveRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/leave-requests")
@RequiredArgsConstructor
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;
    private final LeaveTypeRepository leaveTypeRepository;

    @PostMapping("/submit")
    public ResponseEntity<?> submitRequest(
            @RequestParam Long employeeId,
            @RequestParam Long leaveTypeId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        try {
            LeaveType leaveType = leaveTypeRepository.findById(leaveTypeId)
                    .orElseThrow(() -> new RuntimeException("Tipul de concediu nu a fost găsit."));

            LeaveRequest savedRequest = leaveRequestService.submitLeaveRequest(employeeId, leaveType, startDate, endDate);

            return ResponseEntity.ok(savedRequest);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
package com.leavehub.backend.controller;

import com.leavehub.backend.dto.NewLeaveRequestDTO;
import com.leavehub.backend.entity.Employee;
import com.leavehub.backend.entity.LeaveRequest;
import com.leavehub.backend.entity.LeaveType;
import com.leavehub.backend.repository.EmployeeRepository;
import com.leavehub.backend.repository.LeaveTypeRepository;
import com.leavehub.backend.service.LeaveRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/leave-requests")
@RequiredArgsConstructor
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;
    private final LeaveTypeRepository leaveTypeRepository;
    private final EmployeeRepository employeeRepository;

    @PostMapping
    public ResponseEntity<?> submitRequest(@RequestBody NewLeaveRequestDTO requestDTO) {
        try {
            Employee employee = employeeRepository.findByEmail(requestDTO.getEmail())
                    .orElseThrow(() -> new RuntimeException("Angajatul nu a fost gasit."));

            LeaveType leaveType = leaveTypeRepository.findById(requestDTO.getLeaveTypeId())
                    .orElseThrow(() -> new RuntimeException("Tipul de concediu nu a fost gasit."));

            LeaveRequest savedRequest = leaveRequestService.submitLeaveRequest(
                    employee.getId(),
                    leaveType,
                    requestDTO.getStartDate(),
                    requestDTO.getEndDate()
            );

            return ResponseEntity.ok(savedRequest);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
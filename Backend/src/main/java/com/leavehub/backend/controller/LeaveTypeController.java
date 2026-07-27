package com.leavehub.backend.controller;

import com.leavehub.backend.entity.LeaveType;
import com.leavehub.backend.service.LeaveTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/leave-types")
@RequiredArgsConstructor
public class LeaveTypeController {

    private final LeaveTypeService leaveTypeService;

    @GetMapping
    public ResponseEntity<List<LeaveType>> getAllLeaveTypes() {
        List<LeaveType> leaveTypes = leaveTypeService.getAllLeaveTypes();
        return ResponseEntity.ok(leaveTypes);
    }
}
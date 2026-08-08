package com.leavehub.backend.controller;

import com.leavehub.backend.dto.ManagerRequestDTO;
import com.leavehub.backend.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;

    @GetMapping("/requests")
    public ResponseEntity<?> getDepartmentRequests(@RequestParam String email) {
        try {
            return ResponseEntity.ok(managerService.getDepartmentRequests(email));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/requests/{id}/approve")
    public ResponseEntity<?> approveRequest(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        try {
            managerService.processRequest(id, payload.get("email"), "APPROVED", payload.get("comment"));
            return ResponseEntity.ok().body(Map.of("message", "Cerere aprobată cu succes."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/requests/{id}/reject")
    public ResponseEntity<?> rejectRequest(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        try {
            managerService.processRequest(id, payload.get("email"), "REJECTED", payload.get("comment"));
            return ResponseEntity.ok().body(Map.of("message", "Cerere respinsă."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
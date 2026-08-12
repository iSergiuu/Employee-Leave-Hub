package com.leavehub.backend.controller;

import com.leavehub.backend.dto.NewLeaveRequestDTO;
import com.leavehub.backend.entity.LeaveRequest;
import com.leavehub.backend.service.LeaveRequestService;
import com.leavehub.backend.service.PdfGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/leave-requests")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;
    private final PdfGeneratorService pdfGeneratorService;

    @PostMapping
    public ResponseEntity<?> submitRequest(@RequestBody NewLeaveRequestDTO requestDTO) {
        try {
            LeaveRequest savedRequest = leaveRequestService.submitLeaveRequest(requestDTO);
            return ResponseEntity.ok(Map.of(
                    "message", "Cererea a fost creată cu succes.",
                    "id", savedRequest.getId()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelRequest(@PathVariable Long id) {
        try {
            leaveRequestService.cancelRequest(id);
            return ResponseEntity.ok(Map.of("message", "Cererea a fost anulată cu succes."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long id) {
        try {
            LeaveRequest request = leaveRequestService.findById(id);
            byte[] pdfBytes = pdfGeneratorService.generateLeaveRequestPdf(request);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=cerere_concediu_" + id + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // --- ENDPOINT PENTRU CALENDARUL DE DEPARTAMENT (CU MAPARE MANUALĂ) ---
    @GetMapping("/department")
    public ResponseEntity<?> getDepartmentRequests(@RequestParam String email) {
        try {
            List<LeaveRequest> requests = leaveRequestService.getDepartmentRequestsByEmail(email);

            // Construim manual pachetul JSON pentru a ocoli orice blocaj din Jackson/Hibernate
            List<Map<String, Object>> responseList = new java.util.ArrayList<>();

            for (LeaveRequest req : requests) {
                Map<String, Object> map = new java.util.HashMap<>();
                map.put("id", req.getId());
                map.put("startDate", req.getStartDate());
                map.put("endDate", req.getEndDate());
                map.put("status", req.getStatus());
                map.put("workingDays", req.getWorkingDays());
                map.put("createdAt", req.getCreatedAt());

                // Adăugăm doar datele esențiale din relații, evitând tot obiectul Employee care poate cauza probleme
                if (req.getLeaveType() != null) {
                    map.put("leaveType", Map.of("code", req.getLeaveType().getCode()));
                }
                if (req.getEmployee() != null) {
                    map.put("employee", Map.of("name", req.getEmployee().getName()));
                }

                responseList.add(map);
            }

            return ResponseEntity.ok(responseList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
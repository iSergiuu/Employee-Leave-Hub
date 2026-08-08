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

@RestController
@RequestMapping("/api/leave-requests")
@RequiredArgsConstructor
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;
    private final PdfGeneratorService pdfGeneratorService;

    @PostMapping
    public ResponseEntity<?> submitRequest(@RequestBody NewLeaveRequestDTO requestDTO) {
        try {
            // Trimitem întregul DTO către serviciu. El va căuta angajatul, tipul, va calcula zilele și va salva.
            LeaveRequest savedRequest = leaveRequestService.submitLeaveRequest(requestDTO);
            return ResponseEntity.ok(savedRequest);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelRequest(@PathVariable Long id) {
        try {
            LeaveRequest request = leaveRequestService.cancelRequest(id);
            return ResponseEntity.ok(request);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
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
}
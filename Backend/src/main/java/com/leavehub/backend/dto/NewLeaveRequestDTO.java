package com.leavehub.backend.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class NewLeaveRequestDTO {
    private String email;
    private Long leaveTypeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
}
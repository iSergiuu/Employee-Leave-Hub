package com.leavehub.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class LeaveRequestHistoryDTO {
    private Long id;
    private LeaveTypeDTO leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private int days;
    private String status;
    private LocalDate actionDate;

    @Data
    @AllArgsConstructor
    public static class LeaveTypeDTO {
        private Long id;
        private String name;
    }
}
package com.leavehub.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LeaveRequestHistoryDTO {
    private Long id;
    private LeaveTypeDTO leaveType;
    private String startDate;
    private String endDate;
    private int days;
    private String status;
    private String actionDate;

    @Data
    @AllArgsConstructor
    public static class LeaveTypeDTO {
        private Long id;
        private String name;
    }
}
package com.leavehub.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManagerRequestDTO {
    private Long id;
    private String employeeName;
    private String leaveType;
    private String period;
    private int workingDays;
    private String status;
    private String createdAt;
}
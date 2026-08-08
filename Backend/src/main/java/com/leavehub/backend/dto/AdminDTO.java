package com.leavehub.backend.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class AdminDTO {

    @Data
    public static class EmployeeInfo {
        private Long id;
        private String name;
        private String email;
        private String role;
        private Long deptId;
    }

    @Data
    public static class DepartmentInfo {
        private Long id;
        private String name;
        private Integer maxAbsentEmployees;

        // Câmpurile noi necesare pentru frontend:
        private String managerName;
        private List<String> employeeNames;
    }

    @Data
    public static class HolidayInfo {
        private Long id;
        private LocalDate holidayDate;
        private String description;
    }

    @Data
    public static class TimelineEventInfo {
        private Long id;
        private Long requestId;
        private String employeeName;
        private String currentStatus;
        private LocalDateTime changedAt;
        private String comments;
    }
}
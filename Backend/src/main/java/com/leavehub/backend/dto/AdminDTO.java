package com.leavehub.backend.dto;

import lombok.Data;
import java.time.LocalDate;

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
    }

    @Data
    public static class HolidayInfo {
        private Long id;
        private LocalDate holidayDate;
        private String description;
    }
}
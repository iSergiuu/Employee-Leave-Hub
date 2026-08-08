package com.leavehub.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDashboardDTO {
    private StatsDTO summaryStats;
    private List<RequestDTO> recentRequests;
    private List<BalanceDTO> leaveBalances;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StatsDTO {
        private int availableDays;
        private int usedDays;
        private int pendingRequests;
        private int approvedRequests;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RequestDTO {
        private Long id;
        private String type;
        private String period;
        private int days;
        private String status;
        private String actionDate;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BalanceDTO {
        private String type;
        private int used;
        private int total;
        private String colorClass;
    }
}
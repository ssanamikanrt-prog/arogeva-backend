package com.hospital.Arogeva.payload;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDashboardResponse {

    private double projectCompletionRate;
    private int totalProjects;
    private int completedProjects;
    private int activeProjects;
    private int overdueProjects;
    private long totalOverdueDays;

    private List<ProjectStatusChart> projectsByStatus;
    private List<ProjectManDaysChart> plannedVsActualManDays;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectStatusChart {
        private String status;
        private int count;
        private double percentage;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectManDaysChart {
        private String projectName;
        private BigDecimal plannedManDays;
        private BigDecimal actualManDays;
    }
}

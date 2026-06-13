package com.hospital.Arogeva.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExecutiveDashboardResponse {

    private TopLevelMetrics topLevelMetrics;
    private ProjectDetails projectDetails;
    private BudgetToActuals budgetToActuals;
    private List<TeamOverviewItem> teamOverview;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopLevelMetrics {


        private BigDecimal plannedManDays;
        private BigDecimal actualManDays;
        private BigDecimal mdRemaining;
        private BigDecimal budgetBurnt;
        private String burnRate;



    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectDetails {


        private String projectName;
        private String architecture;
        private String timeline;
        private String rate;
        private BigDecimal totalBudget;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BudgetToActuals {


        private BigDecimal estimatedBudget;
        private BigDecimal budgetBurnt;
        private BigDecimal budgetLeft;
        private String actualEffortSource;
        private String overallStatus;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamOverviewItem {


        private String initials;
        private String roleName;
        private String description;
        private BigDecimal planned;
        private BigDecimal actual;
        private BigDecimal cost;
        private String status;

    }


}

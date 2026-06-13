package com.hospital.Arogeva.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyDashboardResponse {

    private BigDecimal selectedWeekEffort;
    private BigDecimal selectedWeekHours;
    private BigDecimal selectedWeekCost;
    private BigDecimal dailyAverage;
    private Integer entriesCount;
    private List<ResourceEffortRow> resourceRows;
    private ResourceEffortRow dailyTotal;




    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResourceEffortRow {

        private Integer resourceId;
        private Integer moduleId;
        private String resourceName;
        private String role;
        private String moduleName;
        private List<DailyEffortData> dailyData;
        private BigDecimal weekMd;
        private BigDecimal weekCost;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyEffortData {
        private String date; 
        private String dayOfWeek; 
        private BigDecimal manDays;

    }


}

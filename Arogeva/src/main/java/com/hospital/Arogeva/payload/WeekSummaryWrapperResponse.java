package com.hospital.Arogeva.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeekSummaryWrapperResponse {

    private List<WeekSummaryResponse> data;
    private WeekTotalMetrics total;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WeekTotalMetrics {
        private BigDecimal totalHours;
        private BigDecimal totalManDays;
        private BigDecimal totalCost;
        private Integer totalEntries;
    }
}

package com.hospital.Arogeva.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModuleSummaryWrapperResponse {

    private List<ModuleSummaryResponse> modules;
    private ModuleTotalMetrics total;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModuleTotalMetrics {
        private BigDecimal plannedMd;
        private BigDecimal plannedCost;
        private BigDecimal actualMd;
        private String consumedPercent;

    }

}

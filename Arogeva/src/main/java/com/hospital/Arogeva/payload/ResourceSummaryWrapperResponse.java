package com.hospital.Arogeva.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceSummaryWrapperResponse {

    private List<ResourceSummaryResponse> resources;
    private TotalMetrics total;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TotalMetrics {
        private BigDecimal plannedMd;
        private BigDecimal actualMd;
        private BigDecimal mdLeft;
        private BigDecimal costBurnt;

    }

}

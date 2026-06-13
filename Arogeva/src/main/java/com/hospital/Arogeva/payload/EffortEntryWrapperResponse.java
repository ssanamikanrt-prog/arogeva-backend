package com.hospital.Arogeva.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EffortEntryWrapperResponse {

    private List<EffortEntryResponse> entries;
    private EffortTotalMetrics total;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EffortTotalMetrics {

        private BigDecimal totalHours;
        private BigDecimal totalMd;


    }


}

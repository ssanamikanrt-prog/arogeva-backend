package com.hospital.Arogeva.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModuleSummaryResponse {

    private Integer moduleId;
    private String moduleName;
    private String scope;
    private BigDecimal plannedMd;
    private BigDecimal plannedCost;
    private BigDecimal actualMd;
    private String consumedPercent;


}

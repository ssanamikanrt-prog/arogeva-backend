package com.hospital.Arogeva.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceSummaryResponse {

    private Integer resourceId;
    private String resourceName;
    private String role;
    private String level;
    private String moduleName;
    private BigDecimal ratePerDay;
    private BigDecimal plannedMd;
    private BigDecimal actualMd;
    private BigDecimal mdLeft;
    private BigDecimal costBurnt;
    private String status;

}

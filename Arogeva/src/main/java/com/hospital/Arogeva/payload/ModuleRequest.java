package com.hospital.Arogeva.payload;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ModuleRequest {

    private Integer moduleId;
    private Integer projectId;
    private String moduleName;
    private String scopeDescription;
    private BigDecimal plannedManDays;
    private BigDecimal plannedCost;


}

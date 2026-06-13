package com.hospital.Arogeva.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModuleResponse {

    private Integer moduleId;
    private Integer projectId;
    private String projectName;
    private String moduleName;
    private String scopeDescription;
    private BigDecimal plannedManDays;
    private BigDecimal plannedCost;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String message;
    private boolean success;


}

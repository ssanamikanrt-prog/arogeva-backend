package com.hospital.Arogeva.payload;

import lombok.Data;
import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Data
public class ModuleRequest {

    private Integer moduleId;

    @NotNull(message = "Project ID is required")
    private Integer projectId;

    @NotBlank(message = "Module name is required")
    private String moduleName;

    private String scopeDescription;

    @NotNull(message = "Planned man days is required")
    @PositiveOrZero(message = "Planned man days cannot be negative")
    private BigDecimal plannedManDays;

    @NotNull(message = "Planned cost is required")
    @PositiveOrZero(message = "Planned cost cannot be negative")
    private BigDecimal plannedCost;


}

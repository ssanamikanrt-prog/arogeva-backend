package com.hospital.Arogeva.payload;

import lombok.Data;
import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Data
public class ResourceRequest {

    private Integer resourceId;

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotNull(message = "Developer Type ID is required")
    private Integer developerTypeId;

    @NotNull(message = "Project ID is required")
    private Integer projectId;

    @NotNull(message = "Module ID is required")
    private Integer moduleId;

    private String experienceLevel;

    @NotNull(message = "Rate per day is required")
    @PositiveOrZero(message = "Rate per day cannot be negative")
    private BigDecimal ratePerDay;

    @NotNull(message = "Planned man days is required")
    @PositiveOrZero(message = "Planned man days cannot be negative")
    private BigDecimal plannedManDays;

    private String remarks;


}

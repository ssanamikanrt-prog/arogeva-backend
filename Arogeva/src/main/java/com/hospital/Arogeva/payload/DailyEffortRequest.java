package com.hospital.Arogeva.payload;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Data
public class DailyEffortRequest {

    private Integer entryId;
    
    @NotNull(message = "Resource is required")
    private Integer resourceId;

    @NotNull(message = "Project is required")
    private Integer projectId;

    @NotNull(message = "Week is required")
    private Integer weekId;

    @NotNull(message = "Module is required")
    private Integer moduleId;

    @NotNull(message = "Activity is required")
    private Integer activityId;

    @NotNull(message = "Status is required")
    private Integer statusId;
    
    @NotNull(message = "Work date is required")
    private LocalDate workDate;

    @NotNull(message = "Hours worked is required")
    @PositiveOrZero(message = "Hours cannot be negative")
    private BigDecimal hoursWorked;

    @NotNull(message = "Man days is required")
    @PositiveOrZero(message = "Man days cannot be negative")
    private BigDecimal manDays;

    private String workDescription;


}

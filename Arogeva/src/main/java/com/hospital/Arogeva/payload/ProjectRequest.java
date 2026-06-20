package com.hospital.Arogeva.payload;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.AssertTrue;

@Data
public class ProjectRequest {

    private Integer projectId;

    @NotBlank(message = "Project name cannot be empty")
    private String projectName;

    private String description;

    @NotNull(message = "Planned man days is required")
    @Positive(message = "Planned man days must be a positive number")
    private BigDecimal plannedManDays;

    @NotNull(message = "Planned budget is required")
    @PositiveOrZero(message = "Planned budget cannot be negative")
    private BigDecimal plannedBudget;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    private String architecture;

    @NotBlank(message = "Status cannot be empty")
    private String status;
    
    private String projectManager;
    private String client;

    @AssertTrue(message = "Start date must be before or equal to the end date")
    public boolean isDateValid() {
        // If either is null, let the @NotNull annotations handle it
        if (startDate == null || endDate == null) {
            return true; 
        }
        return !startDate.isAfter(endDate); // Returns true if start date is not after end date
    }

}

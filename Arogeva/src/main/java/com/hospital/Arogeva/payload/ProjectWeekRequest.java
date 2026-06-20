package com.hospital.Arogeva.payload;

import lombok.Data;
import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.AssertTrue;

@Data
public class ProjectWeekRequest {


    private Integer weekId;

    @NotNull(message = "Project ID is required")
    private Integer projectId;

    @NotBlank(message = "Week name is required")
    private String weekName;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @AssertTrue(message = "Start date must be before or equal to the end date")
    public boolean isDateValid() {
        if (startDate == null || endDate == null) {
            return true; 
        }
        return !startDate.isAfter(endDate); 
    }


}

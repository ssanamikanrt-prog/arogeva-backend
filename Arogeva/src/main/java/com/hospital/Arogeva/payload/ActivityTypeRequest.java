package com.hospital.Arogeva.payload;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class ActivityTypeRequest {

    private Integer activityId;

    @NotBlank(message = "Activity name is required")
    private String activityName;

    @NotBlank(message = "Activity status is required")
    private String activityStatus;


}

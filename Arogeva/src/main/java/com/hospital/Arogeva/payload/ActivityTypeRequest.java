package com.hospital.Arogeva.payload;

import lombok.Data;

@Data
public class ActivityTypeRequest {

    private Integer activityId;
    private String activityName;
    private String activityStatus;


}

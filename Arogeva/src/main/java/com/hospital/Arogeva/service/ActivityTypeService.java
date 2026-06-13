package com.hospital.Arogeva.service;

import com.hospital.Arogeva.payload.ActivityTypeRequest;
import com.hospital.Arogeva.payload.ActivityTypeResponse;
import java.util.List;

public interface ActivityTypeService {
    

    ActivityTypeResponse createOrUpdateActivityType(ActivityTypeRequest request);
    

    List<ActivityTypeResponse> getAllActivityTypes();
    

    ActivityTypeResponse getActivityTypeById(Integer activityId);
    

    ActivityTypeResponse deleteActivityType(Integer activityId);
}

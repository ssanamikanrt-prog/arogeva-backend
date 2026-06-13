package com.hospital.Arogeva.service.impl;

import com.hospital.Arogeva.entity.ActivityType;
import com.hospital.Arogeva.payload.ActivityTypeRequest;
import com.hospital.Arogeva.payload.ActivityTypeResponse;
import com.hospital.Arogeva.repository.ActivityTypeRepository;
import com.hospital.Arogeva.service.ActivityTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ActivityTypeServiceImpl implements ActivityTypeService {

    @Autowired
    private ActivityTypeRepository activityTypeRepository;

    @Override
    public ActivityTypeResponse createOrUpdateActivityType(ActivityTypeRequest request) {
        ActivityTypeResponse response = new ActivityTypeResponse();
        
        try {
            ActivityType activityType;
            

            if (request.getActivityId() != null) {
                Optional<ActivityType> existing = activityTypeRepository.findById(request.getActivityId());
                
                if (existing.isPresent()) {

                    activityType = existing.get();
                    activityType.setActivityName(request.getActivityName());
                    activityType.setActivityStatus(request.getActivityStatus());
                    activityType.setUpdatedAt(LocalDateTime.now());
                    activityTypeRepository.save(activityType);
                    response.setSuccess(true);
                    response.setMessage("Activity Type updated successfully");
                } else {
                    response.setSuccess(false);
                    response.setMessage("Activity Type with ID " + request.getActivityId() + " not found");
                    return response;
                }
            } else {

                activityType = new ActivityType();
                activityType.setActivityName(request.getActivityName());
                activityType.setActivityStatus(request.getActivityStatus());
                activityType.setCreatedAt(LocalDateTime.now());
                activityTypeRepository.save(activityType);
                response.setSuccess(true);
                response.setMessage("Activity Type created successfully");
            }

            response.setActivityId(activityType.getActivityId());
            response.setActivityName(activityType.getActivityName());
            response.setActivityStatus(activityType.getActivityStatus());
            response.setCreatedAt(activityType.getCreatedAt());
            response.setUpdatedAt(activityType.getUpdatedAt());
            
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Error: " + e.getMessage());
        }
        
        return response;
    }

    @Override
    public List<ActivityTypeResponse> getAllActivityTypes() {
        return activityTypeRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ActivityTypeResponse getActivityTypeById(Integer activityId) {
        Optional<ActivityType> activityType = activityTypeRepository.findById(activityId);
        
        if (activityType.isPresent()) {
            ActivityTypeResponse response = convertToResponse(activityType.get());
            response.setSuccess(true);
            return response;
        }
        
        ActivityTypeResponse response = new ActivityTypeResponse();
        response.setSuccess(false);
        response.setMessage("Activity Type with ID " + activityId + " not found");
        return response;
    }

    @Override
    public ActivityTypeResponse deleteActivityType(Integer activityId) {
        ActivityTypeResponse response = new ActivityTypeResponse();
        
        try {
            Optional<ActivityType> activityType = activityTypeRepository.findById(activityId);
            
            if (activityType.isPresent()) {
                activityTypeRepository.deleteById(activityId);
                response.setSuccess(true);
                response.setMessage("Activity Type deleted successfully");
            } else {
                response.setSuccess(false);
                response.setMessage("Activity Type with ID " + activityId + " not found");
            }
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Error: " + e.getMessage());
        }
        
        return response;
    }


    private ActivityTypeResponse convertToResponse(ActivityType activityType) {
        ActivityTypeResponse response = new ActivityTypeResponse();
        response.setActivityId(activityType.getActivityId());
        response.setActivityName(activityType.getActivityName());
        response.setActivityStatus(activityType.getActivityStatus());
        response.setCreatedAt(activityType.getCreatedAt());
        response.setUpdatedAt(activityType.getUpdatedAt());
        response.setSuccess(true);
        return response;
    }
}

package com.hospital.Arogeva.controller;

import com.hospital.Arogeva.advices.ApiResponse;

import com.hospital.Arogeva.payload.ActivityTypeRequest;
import com.hospital.Arogeva.payload.ActivityTypeResponse;
import com.hospital.Arogeva.service.ActivityTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/activity-types")
@CrossOrigin(origins = "*")
@Tag(name = "Activity Types", description = "Endpoints for managing Activity Types")
public class ActivityTypeController {

    @Autowired
    private ActivityTypeService activityTypeService;

    @Operation(summary = "Create or Update Activity Type", description = "Creates a new Activity Type if activityId is not provided, or updates existing if ID is provided")
    @PostMapping("/create-or-update")
    public ResponseEntity<ApiResponse<ActivityTypeResponse>> createOrUpdateActivityType(@RequestBody ActivityTypeRequest request) {
        ActivityTypeResponse response = activityTypeService.createOrUpdateActivityType(request);
        return ResponseEntity.ok(new ApiResponse<>(response));
    }

    @Operation(summary = "Get All Activity Types", description = "Fetches all available Activity Types")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<ActivityTypeResponse>>> getAllActivityTypes() {
        List<ActivityTypeResponse> response = activityTypeService.getAllActivityTypes();
        return ResponseEntity.ok(new ApiResponse<>(response));
    }

    @Operation(summary = "Get Activity Type By ID", description = "Fetches a specific Activity Type by its ID")
    @GetMapping("/{activityId}")
    public ResponseEntity<ApiResponse<ActivityTypeResponse>> getActivityTypeById(@PathVariable Integer activityId) {
        ActivityTypeResponse response = activityTypeService.getActivityTypeById(activityId);
        return ResponseEntity.ok(new ApiResponse<>(response));
    }

    @Operation(summary = "Delete Activity Type", description = "Deletes an Activity Type by its ID")
    @DeleteMapping("/{activityId}")
    public ResponseEntity<ApiResponse<ActivityTypeResponse>> deleteActivityType(@PathVariable Integer activityId) {
        ActivityTypeResponse response = activityTypeService.deleteActivityType(activityId);
        return ResponseEntity.ok(new ApiResponse<>(response));
    }



}

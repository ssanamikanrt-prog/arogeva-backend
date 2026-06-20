package com.hospital.Arogeva.controller;

import com.hospital.Arogeva.advices.ApiResponse;

import com.hospital.Arogeva.payload.DailyEffortRequest;
import com.hospital.Arogeva.payload.DailyEffortResponse;
import com.hospital.Arogeva.payload.ResourceDropdownResponse;
import com.hospital.Arogeva.service.DailyEffortService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.security.Principal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import com.hospital.Arogeva.payload.ProjectWeekResponse;
import com.hospital.Arogeva.payload.EffortEntryWrapperResponse;

@RestController
@RequestMapping("/api/effort")
@Tag(name = "Daily Effort Tracking", description = "Endpoints for creating and updating daily effort logs")
public class DailyEffortController {

    @Autowired
    private DailyEffortService dailyEffortService;




    @Operation(summary = "Save or Update Effort Entry", description = "Creates a new daily effort entry if entryId is null, otherwise updates the existing entry.")
    @PostMapping("/save")
    public ResponseEntity<ApiResponse<DailyEffortResponse>> saveOrUpdateEffort(@Valid @RequestBody DailyEffortRequest request, Principal principal) {

        String currentUserId = principal != null ? principal.getName() : null;

        DailyEffortResponse response = dailyEffortService.saveOrUpdateEffort(request, currentUserId);

        return ResponseEntity.ok(new ApiResponse<>(response));
    }

    @Operation(summary = "Get Project Weeks", description = "Retrieves all project weeks to populate the Week / Sprint dropdown")
    @GetMapping("/weeks")
    public ResponseEntity<ApiResponse<List<ProjectWeekResponse>>> getAllWeeks() {

        return ResponseEntity.ok(new ApiResponse<>(dailyEffortService.getAllProjectWeeks()));

    }

    @Operation(summary = "Get All Resources", description = "Retrieves all resources for dropdown filtering")
    @GetMapping("/resources")
    public ResponseEntity<ApiResponse<List<ResourceDropdownResponse>>> getAllResources() {

        return ResponseEntity.ok(new ApiResponse<>(dailyEffortService.getAllResources()));

    }

    @Operation(summary = "Get Daily Effort Logs by Week", description = "Retrieves all effort logs for a specific week of the logged-in user")
    @GetMapping("/my-logs/{weekId}")
    public ResponseEntity<ApiResponse<EffortEntryWrapperResponse>> getMyEffortLogs(
            @PathVariable Integer weekId, 
            @RequestParam(required = false) Integer projectId,
            Principal principal) {

        String currentUserId = principal != null ? principal.getName() : null;

        return ResponseEntity.ok(new ApiResponse<>(dailyEffortService.getMyEffortEntries(weekId, currentUserId, projectId)));

    }
}

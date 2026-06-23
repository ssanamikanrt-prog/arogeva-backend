package com.hospital.Arogeva.controller;

import com.hospital.Arogeva.advices.ApiResponse;

import com.hospital.Arogeva.payload.ProjectWeekRequest;
import com.hospital.Arogeva.payload.ProjectWeekResponse;
import com.hospital.Arogeva.payload.ProjectWeekResponseDTO;
import com.hospital.Arogeva.service.ProjectWeekService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/project-weeks")
@CrossOrigin(origins = "*")
@Tag(name = "Project Weeks", description = "Endpoints for managing Project Weeks")
public class ProjectWeekController {

    @Autowired
    private ProjectWeekService projectWeekService;

    @Operation(summary = "Create or Update Project Week", description = "Creates a new Project Week if weekId is not provided, or updates existing if ID is provided")
    @PostMapping("/create-or-update")
    public ResponseEntity<ApiResponse<ProjectWeekResponse>> createOrUpdateProjectWeek(@Valid @RequestBody ProjectWeekRequest request) {

        ProjectWeekResponse response = projectWeekService.createOrUpdateProjectWeek(request);

        return ResponseEntity.ok(new ApiResponse<>(response));

    }


    @Operation(summary = "Get All Project Weeks", description = "Fetches all available Project Weeks")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<ProjectWeekResponse>>> getAllProjectWeeks() {

        List<ProjectWeekResponse> response = projectWeekService.getAllProjectWeeks();

        return ResponseEntity.ok(new ApiResponse<>(response));

    }

    @Operation(summary = "Get Project Week By ID", description = "Fetches a specific Project Week by its ID")
    @GetMapping("/{weekId}")
    public ResponseEntity<ApiResponse<ProjectWeekResponse>> getProjectWeekById(@PathVariable Integer weekId) {

        ProjectWeekResponse response = projectWeekService.getProjectWeekById(weekId);

        return ResponseEntity.ok(new ApiResponse<>(response));

    }

    @Operation(summary = "Get Weeks By Project ID", description = "Fetches all weeks for a specific project")
    @GetMapping("/project/{projectId}")
    public ResponseEntity<ApiResponse<List<ProjectWeekResponse>>> getWeeksByProjectId(@PathVariable Integer projectId) {

        List<ProjectWeekResponse> response = projectWeekService.getWeeksByProjectId(projectId);

        return ResponseEntity.ok(new ApiResponse<>(response));

    }

    @Operation(summary = "Delete Project Week", description = "Deletes a Project Week by its ID")
    @DeleteMapping("/{weekId}")
    public ResponseEntity<ApiResponse<ProjectWeekResponse>> deleteProjectWeek(@PathVariable Integer weekId) {

        ProjectWeekResponse response = projectWeekService.deleteProjectWeek(weekId);

        return ResponseEntity.ok(new ApiResponse<>(response));

    }


        @Operation(summary = "get All Weeks", description = "Get All Weeks")
        @GetMapping
        public ResponseEntity<ApiResponse<List<ProjectWeekResponseDTO>>> getAllWeeks() {

            return ResponseEntity.ok(new ApiResponse<>(projectWeekService.getAllWeeks()));

        }

}

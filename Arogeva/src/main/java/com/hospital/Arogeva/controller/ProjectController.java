package com.hospital.Arogeva.controller;

import com.hospital.Arogeva.advices.ApiResponse;

import com.hospital.Arogeva.payload.ProjectRequest;
import com.hospital.Arogeva.payload.ProjectResponse;
import com.hospital.Arogeva.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "*")
@Tag(name = "Projects", description = "Endpoints for managing Projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Operation(summary = "Create or Update Project", description = "Creates a new Project if projectId is not provided, or updates existing if ID is provided")
    @PostMapping("/create-or-update")
    public ResponseEntity<ApiResponse<ProjectResponse>> createOrUpdateProject(@RequestBody ProjectRequest request) {
        ProjectResponse response = projectService.createOrUpdateProject(request);
        return ResponseEntity.ok(new ApiResponse<>(response));
    }


    @Operation(summary = "Get All Projects", description = "Fetches all available Projects")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> getAllProjects() {
        List<ProjectResponse> response = projectService.getAllProjects();
        return ResponseEntity.ok(new ApiResponse<>(response));
    }

    @Operation(summary = "Get Project By ID", description = "Fetches a specific Project by its ID")
    @GetMapping("/{projectId}")
    public ResponseEntity<ApiResponse<ProjectResponse>> getProjectById(@PathVariable Integer projectId) {
        ProjectResponse response = projectService.getProjectById(projectId);
        return ResponseEntity.ok(new ApiResponse<>(response));
    }

    @Operation(summary = "Delete Project", description = "Deletes a Project by its ID")
    @DeleteMapping("/{projectId}")
    public ResponseEntity<ApiResponse<ProjectResponse>> deleteProject(@PathVariable Integer projectId) {
        ProjectResponse response = projectService.deleteProject(projectId);
        return ResponseEntity.ok(new ApiResponse<>(response));
    }
}

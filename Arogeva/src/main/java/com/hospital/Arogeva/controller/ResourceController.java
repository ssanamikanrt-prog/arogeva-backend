package com.hospital.Arogeva.controller;

import com.hospital.Arogeva.advices.ApiResponse;

import com.hospital.Arogeva.payload.ResourceRequest;
import com.hospital.Arogeva.payload.ResourceResponse;
import com.hospital.Arogeva.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/resources")
@CrossOrigin(origins = "*")
@Tag(name = "Resources", description = "Endpoints for managing Resources")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @Operation(summary = "Create or Update Resource", description = "Creates a new Resource if resourceId is not provided, or updates existing if ID is provided")
    @PostMapping("/create-or-update")
    public ResponseEntity<ApiResponse<ResourceResponse>> createOrUpdateResource(@Valid @RequestBody ResourceRequest request) {

        ResourceResponse response = resourceService.createOrUpdateResource(request);

        return ResponseEntity.ok(new ApiResponse<>(response));

    }

    @Operation(summary = "Get All Resources", description = "Fetches all available Resources")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<ResourceResponse>>> getAllResources() {

        List<ResourceResponse> response = resourceService.getAllResources();

        return ResponseEntity.ok(new ApiResponse<>(response));

    }

    @Operation(summary = "Get Resource By ID", description = "Fetches a specific Resource by its ID")
    @GetMapping("/{resourceId}")
    public ResponseEntity<ApiResponse<ResourceResponse>> getResourceById(@PathVariable Integer resourceId) {

        ResourceResponse response = resourceService.getResourceById(resourceId);

        return ResponseEntity.ok(new ApiResponse<>(response));

    }

    @Operation(summary = "Get Resources By Project ID", description = "Fetches all resources assigned to a specific project")
    @GetMapping("/project/{projectId}")
    public ResponseEntity<ApiResponse<List<ResourceResponse>>> getResourcesByProjectId(@PathVariable Integer projectId) {

        List<ResourceResponse> response = resourceService.getResourcesByProjectId(projectId);

        return ResponseEntity.ok(new ApiResponse<>(response));

    }

    @Operation(summary = "Get Resources By Module ID", description = "Fetches all resources assigned to a specific module")
    @GetMapping("/module/{moduleId}")
    public ResponseEntity<ApiResponse<List<ResourceResponse>>> getResourcesByModuleId(@PathVariable Integer moduleId) {

        List<ResourceResponse> response = resourceService.getResourcesByModuleId(moduleId);

        return ResponseEntity.ok(new ApiResponse<>(response));

    }

    @Operation(summary = "Get Resources By User ID", description = "Fetches all resources assigned to a specific user")
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<ResourceResponse>>> getResourcesByUserId(@PathVariable String userId) {

        List<ResourceResponse> response = resourceService.getResourcesByUserId(userId);

        return ResponseEntity.ok(new ApiResponse<>(response));

    }

    @Operation(summary = "Delete Resource", description = "Deletes a Resource by its ID")
    @DeleteMapping("/{resourceId}")
    public ResponseEntity<ApiResponse<ResourceResponse>> deleteResource(@PathVariable Integer resourceId) {

        ResourceResponse response = resourceService.deleteResource(resourceId);

        return ResponseEntity.ok(new ApiResponse<>(response));

    }
}

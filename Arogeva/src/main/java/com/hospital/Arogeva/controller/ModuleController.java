package com.hospital.Arogeva.controller;

import com.hospital.Arogeva.advices.ApiResponse;

import com.hospital.Arogeva.payload.ModuleRequest;
import com.hospital.Arogeva.payload.ModuleResponse;
import com.hospital.Arogeva.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/modules")
@CrossOrigin(origins = "*")
@Tag(name = "Modules", description = "Endpoints for managing Modules")
public class ModuleController {

    @Autowired
    private ModuleService moduleService;

    @Operation(summary = "Create or Update Module", description = "Creates a new Module if moduleId is not provided, or updates existing if ID is provided")
    @PostMapping("/create-or-update")
    public ResponseEntity<ApiResponse<ModuleResponse>> createOrUpdateModule(@Valid @RequestBody ModuleRequest request) {

        ModuleResponse response = moduleService.createOrUpdateModule(request);


        return ResponseEntity.ok(new ApiResponse<>(response));

    }

    @Operation(summary = "Get All Modules", description = "Fetches all available Modules")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<ModuleResponse>>> getAllModules() {

        List<ModuleResponse> response = moduleService.getAllModules();

        return ResponseEntity.ok(new ApiResponse<>(response));

    }

    @Operation(summary = "Get Module By ID", description = "Fetches a specific Module by its ID")
    @GetMapping("/{moduleId}")
    public ResponseEntity<ApiResponse<ModuleResponse>> getModuleById(@PathVariable Integer moduleId) {

        ModuleResponse response = moduleService.getModuleById(moduleId);

        return ResponseEntity.ok(new ApiResponse<>(response));

    }

    @Operation(summary = "Get Modules By Project ID", description = "Fetches all modules for a specific project")
    @GetMapping("/project/{projectId}")
    public ResponseEntity<ApiResponse<List<ModuleResponse>>> getModulesByProjectId(@PathVariable Integer projectId) {

        List<ModuleResponse> response = moduleService.getModulesByProjectId(projectId);

        return ResponseEntity.ok(new ApiResponse<>(response));

    }

    @Operation(summary = "Delete Module", description = "Deletes a Module by its ID")
    @DeleteMapping("/{moduleId}")
    public ResponseEntity<ApiResponse<ModuleResponse>> deleteModule(@PathVariable Integer moduleId) {

        ModuleResponse response = moduleService.deleteModule(moduleId);

        return ResponseEntity.ok(new ApiResponse<>(response));

    }
}

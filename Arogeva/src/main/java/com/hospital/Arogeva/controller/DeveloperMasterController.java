package com.hospital.Arogeva.controller;

import com.hospital.Arogeva.advices.ApiResponse;

import com.hospital.Arogeva.payload.DeveloperMasterRequest;
import com.hospital.Arogeva.payload.DeveloperMasterResponse;
import com.hospital.Arogeva.service.DeveloperMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/developer-master")
@CrossOrigin(origins = "*")
@Tag(name = "Developer Master", description = "Endpoints for managing Developer Master types")
public class DeveloperMasterController {

    @Autowired
    private DeveloperMasterService developerMasterService;

    @Operation(summary = "Create or Update Developer Master", description = "Creates a new Developer Master if developerTypeId is not provided, or updates existing if ID is provided")
    @PostMapping("/create-or-update")
    public ResponseEntity<ApiResponse<DeveloperMasterResponse>> createOrUpdateDeveloperMaster(@Valid @RequestBody DeveloperMasterRequest request) {

        DeveloperMasterResponse response = developerMasterService.createOrUpdateDeveloperMaster(request);

        return ResponseEntity.ok(new ApiResponse<>(response));

    }

    @Operation(summary = "Get All Developer Masters", description = "Fetches all available Developer Master types")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<DeveloperMasterResponse>>> getAllDeveloperMasters() {

        List<DeveloperMasterResponse> response = developerMasterService.getAllDeveloperMasters();


        return ResponseEntity.ok(new ApiResponse<>(response));

    }

    @Operation(summary = "Get Developer Master By ID", description = "Fetches a specific Developer Master by its ID")
    @GetMapping("/{developerTypeId}")
    public ResponseEntity<ApiResponse<DeveloperMasterResponse>> getDeveloperMasterById(@PathVariable Integer developerTypeId) {

        DeveloperMasterResponse response = developerMasterService.getDeveloperMasterById(developerTypeId);

        return ResponseEntity.ok(new ApiResponse<>(response));

    }

    @Operation(summary = "Delete Developer Master", description = "Deletes a Developer Master by its ID")
    @DeleteMapping("/{developerTypeId}")
    public ResponseEntity<ApiResponse<DeveloperMasterResponse>> deleteDeveloperMaster(@PathVariable Integer developerTypeId) {

        DeveloperMasterResponse response = developerMasterService.deleteDeveloperMaster(developerTypeId);

        return ResponseEntity.ok(new ApiResponse<>(response));

    }
}

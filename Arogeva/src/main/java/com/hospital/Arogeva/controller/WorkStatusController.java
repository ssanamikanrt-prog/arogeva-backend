package com.hospital.Arogeva.controller;

import com.hospital.Arogeva.advices.ApiResponse;

import com.hospital.Arogeva.payload.WorkStatusRequest;
import com.hospital.Arogeva.payload.WorkStatusResponse;
import com.hospital.Arogeva.service.WorkStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/work-statuses")
@CrossOrigin(origins = "*")
@Tag(name = "Work Statuses", description = "Endpoints for managing Work Statuses")
public class WorkStatusController {

    @Autowired
    private WorkStatusService workStatusService;

    @Operation(summary = "Create or Update Work Status", description = "Creates a new Work Status if statusId is not provided, or updates existing if ID is provided")
    @PostMapping("/create-or-update")
    public ResponseEntity<ApiResponse<WorkStatusResponse>> createOrUpdateWorkStatus(@Valid @RequestBody WorkStatusRequest request) {

        WorkStatusResponse response = workStatusService.createOrUpdateWorkStatus(request);

        return ResponseEntity.ok(new ApiResponse<>(response));

    }

    @Operation(summary = "Get All Work Statuses", description = "Fetches all available Work Statuses")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<WorkStatusResponse>>> getAllWorkStatuses() {

        List<WorkStatusResponse> response = workStatusService.getAllWorkStatuses();

        return ResponseEntity.ok(new ApiResponse<>(response));

    }

    @Operation(summary = "Get Work Status By ID", description = "Fetches a specific Work Status by its ID")
    @GetMapping("/{statusId}")
    public ResponseEntity<ApiResponse<WorkStatusResponse>> getWorkStatusById(@PathVariable Integer statusId) {

        WorkStatusResponse response = workStatusService.getWorkStatusById(statusId);

        return ResponseEntity.ok(new ApiResponse<>(response));

    }

    @Operation(summary = "Delete Work Status", description = "Deletes a Work Status by its ID")
    @DeleteMapping("/{statusId}")
    public ResponseEntity<ApiResponse<WorkStatusResponse>> deleteWorkStatus(@PathVariable Integer statusId) {

        WorkStatusResponse response = workStatusService.deleteWorkStatus(statusId);


        return ResponseEntity.ok(new ApiResponse<>(response));

    }
}

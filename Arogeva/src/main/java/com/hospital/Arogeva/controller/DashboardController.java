package com.hospital.Arogeva.controller;

import com.hospital.Arogeva.advices.ApiResponse;
import com.hospital.Arogeva.payload.*;
import com.hospital.Arogeva.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
@Tag(name = "Dashboard", description = "Endpoints for Weekly Calculation Dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @Operation(summary = "Get Summary Executive Dashboard Data")
    @GetMapping("/executive")
    public ResponseEntity<ApiResponse<ExecutiveDashboardResponse>> getExecutiveDashboard(
            @RequestParam(required = false, defaultValue = "1") Integer projectId) {
        return ResponseEntity.ok(new ApiResponse<>(dashboardService.getExecutiveDashboard(projectId)));
    }

    @Operation(summary = "Get Dashboard Data for a Specific Week")
    @GetMapping("/week/{weekId}")
    public ResponseEntity<ApiResponse<WeeklyDashboardResponse>> getWeeklyDashboardData(@PathVariable Integer weekId) {
        return ResponseEntity.ok(new ApiResponse<>(dashboardService.getWeeklyDashboardData(weekId)));
    }

    @Operation(summary = "Get Week-wise Total Summary")
    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<WeekSummaryWrapperResponse>> getWeeklySummaryData() {
        return ResponseEntity.ok(new ApiResponse<>(dashboardService.getWeeklySummaryData()));
    }

    @Operation(summary = "Get Resource Summary")
    @GetMapping("/resources")
    public ResponseEntity<ApiResponse<ResourceSummaryWrapperResponse>> getResourceSummary() {
        return ResponseEntity.ok(new ApiResponse<>(dashboardService.getResourceSummary()));
    }

    @Operation(summary = "Get Module Summary")
    @GetMapping("/modules")
    public ResponseEntity<ApiResponse<ModuleSummaryWrapperResponse>> getModuleSummary() {
        return ResponseEntity.ok(new ApiResponse<>(dashboardService.getModuleSummary()));
    }

    @Operation(summary = "Get All Daily Effort Entries")
    @GetMapping("/entries")
    public ResponseEntity<ApiResponse<EffortEntryWrapperResponse>> getAllEffortEntries() {
        return ResponseEntity.ok(new ApiResponse<>(dashboardService.getAllEffortEntries()));
    }

    @Operation(summary = "Get Daily Effort Entries by Resource ID")
    @GetMapping("/entries/resource/{resourceId}")
    public ResponseEntity<ApiResponse<EffortEntryWrapperResponse>> getEffortEntriesByResource(@PathVariable Integer resourceId) {
        return ResponseEntity.ok(new ApiResponse<>(dashboardService.getEffortEntriesByResource(resourceId)));
    }
}

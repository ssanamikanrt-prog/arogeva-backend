package com.hospital.Arogeva.controller;


import com.hospital.Arogeva.exceptions.ResourceNotFoundException;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.Arogeva.advices.ApiResponse;
import com.hospital.Arogeva.payload.ProjectDashboardResponse;
import com.hospital.Arogeva.service.ProjectDashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.time.YearMonth;

@RestController
@RequestMapping("/api/project-dashboard")
@Tag(name = "Project Overall Dashboard", description = "Endpoints for High-Level Project overall Dashboard")
public class ProjectDashboardController {

    @Autowired
    private ProjectDashboardService projectDashboardService;

    
    @Operation(summary = "Get Project Overall Dashboard Data", description = "Filters: projectId, projectManager, startDate & endDate, OR monthYear (format: YYYY-MM)")
    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<ProjectDashboardResponse>> getDashboardSummary(
            @RequestParam(required = false) Integer projectId,
            @RequestParam(required = false) String projectManager,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String yearMonth) {
        
        // If monthYear is provided (e.g., "2025-11"), it calculates the first and last day of that month
        if (yearMonth != null && !yearMonth.trim().isEmpty() && !yearMonth.equalsIgnoreCase("All Dates")) {
            try {
                YearMonth ym = YearMonth.parse(yearMonth.trim());
                startDate = ym.atDay(1);
                endDate = ym.atEndOfMonth();
            } catch (Exception e) {
                throw new ResourceNotFoundException("Invalid monthYear format. Please use 'YYYY-MM' (e.g., 2025-11)");
            }
        }
        
        return ResponseEntity.ok(new ApiResponse<>(projectDashboardService.getProjectManagementDashboard(projectId, projectManager, startDate, endDate)));
    }
}

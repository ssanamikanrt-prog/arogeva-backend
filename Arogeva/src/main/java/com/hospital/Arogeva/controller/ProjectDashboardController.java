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
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@RestController
@RequestMapping("/api/project-dashboard")
@Tag(name = "Project Overall Dashboard", description = "Endpoints for High-Level Project overall Dashboard")
public class ProjectDashboardController {

    @Autowired
    private ProjectDashboardService projectDashboardService;

    
    @Operation(summary = "Get Project Overall Dashboard Data", description = "Filters: projectId, projectManager, startDate & endDate, OR monthYear (format: YYYY-MM or MMMM, yyyy)")
    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<ProjectDashboardResponse>> getDashboardSummary(
            @RequestParam(required = false) Integer projectId,
            @RequestParam(required = false) String projectManager,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String monthYear,
            @RequestParam(required = false) String yearMonth) {
        
        String filterMonthStr = monthYear != null ? monthYear : yearMonth;
        
        // If monthYear is provided (e.g., "2025-11" or "January, 2026")
        if (filterMonthStr != null && !filterMonthStr.trim().isEmpty() && !filterMonthStr.equalsIgnoreCase("All Dates")) {
            try {
                YearMonth ym;
                String ymStr = filterMonthStr.trim();
                if (ymStr.contains(",")) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM, yyyy", Locale.ENGLISH);
                    ym = YearMonth.parse(ymStr, formatter);
                } else if (ymStr.matches("\\d{2}-\\d{4}")) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-yyyy");
                    ym = YearMonth.parse(ymStr, formatter);
                } else if (ymStr.matches("\\d{2}/\\d{4}")) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
                    ym = YearMonth.parse(ymStr, formatter);
                } else {
                    ym = YearMonth.parse(ymStr);
                }
                startDate = ym.atDay(1);
                endDate = ym.atEndOfMonth();
            } catch (Exception e) {
                throw new ResourceNotFoundException("Invalid monthYear format. Please use 'MM-yyyy' (e.g., 02-2022), 'MMMM, yyyy' (e.g., February, 2022), or 'YYYY-MM'");
            }
        }
        
        // Validate that startDate is not after endDate and endDate is not before startDate
        if (startDate != null && endDate != null) {
            if (startDate.isAfter(endDate) || endDate.isBefore(startDate)) {
                throw new IllegalArgumentException("Start date cannot be after end date, and end date cannot be before start date.");
            }
        }
        
        return ResponseEntity.ok(new ApiResponse<>(projectDashboardService.getProjectManagementDashboard(projectId, projectManager, startDate, endDate)));
    }
}

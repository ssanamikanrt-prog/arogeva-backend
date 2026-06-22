package com.hospital.Arogeva.service.impl;

import com.hospital.Arogeva.entity.Project;
import com.hospital.Arogeva.entity.DailyEffortEntry;
import com.hospital.Arogeva.payload.ProjectDashboardResponse;
import com.hospital.Arogeva.repository.DailyEffortRepository;
import com.hospital.Arogeva.repository.ProjectRepository;
import com.hospital.Arogeva.service.ProjectDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProjectDashboardServiceImpl implements ProjectDashboardService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private DailyEffortRepository dailyEffortRepository;

    @Override
    public ProjectDashboardResponse getProjectManagementDashboard(Integer projectId, String projectManager, LocalDate startDate, LocalDate endDate) {


        List<Project> allProjects;
        
        // 1. Filter Projects by ID
        if (projectId != null) {
            java.util.Optional<Project> p = projectRepository.findById(projectId);
            allProjects = p.isPresent() ? java.util.List.of(p.get()) : new ArrayList<>();
        } else {
            allProjects = projectRepository.findAll();
        }

        // 1.5 Filter Projects by Project Manager
        if (projectManager != null && !projectManager.trim().isEmpty()) {
            allProjects = allProjects.stream()
                .filter(p -> projectManager.equalsIgnoreCase(p.getProjectManager()))
                .collect(java.util.stream.Collectors.toList());
        }

        // 2. Fetch and filter Daily Efforts for accurate Actual Man-Days and dynamic project inclusion
        List<DailyEffortEntry> allEfforts = dailyEffortRepository.findAll();
        
        if (projectId != null) {
            allEfforts = allEfforts.stream()
                .filter(e -> e.getProject() != null && e.getProject().getProjectId().equals(projectId))
                .collect(Collectors.toList());
        }

        if (projectManager != null && !projectManager.trim().isEmpty()) {
            allEfforts = allEfforts.stream()
                .filter(e -> e.getProject() != null && projectManager.equalsIgnoreCase(e.getProject().getProjectManager()))
                .collect(Collectors.toList());
        }
        
        if (startDate != null || endDate != null) {
            allEfforts = allEfforts.stream()
                .filter(e -> {
                    if (e.getWorkDate() == null) return false;
                    boolean afterStart = startDate == null || !e.getWorkDate().isBefore(startDate);
                    boolean beforeEnd = endDate == null || !e.getWorkDate().isAfter(endDate);
                    return afterStart && beforeEnd;
                })
                .collect(Collectors.toList());
        }

        // 3. Filter Projects by Date Range (include any project that overlaps with this range OR has efforts in this range)
        if (startDate != null || endDate != null) {
            java.util.Set<Integer> activeProjectIds = allEfforts.stream()
                .filter(e -> e.getProject() != null)
                .map(e -> e.getProject().getProjectId())
                .collect(Collectors.toSet());

            allProjects = allProjects.stream()
                .filter(p -> {
                    // If the project has logged efforts in this period, it's definitely active
                    if (activeProjectIds.contains(p.getProjectId())) {
                        return true;
                    }
                    
                    LocalDate pStart = p.getStartDate();
                    LocalDate pEnd = p.getEndDate() != null ? p.getEndDate() : LocalDate.MAX;
                    
                    if (pStart == null) return false; // Without a start date and without efforts, we assume it's not active in this period
                    
                    boolean overlaps = true;
                    if (startDate != null) {
                        overlaps = overlaps && !pEnd.isBefore(startDate);
                    }
                    if (endDate != null) {
                        overlaps = overlaps && !pStart.isAfter(endDate);
                    }
                    return overlaps;
                })
                .collect(Collectors.toList());
        }

        int totalProjects = allProjects.size();
        int completedProjects = 0;
        int activeProjects = 0;
        int overdueProjects = 0;
        long totalOverdueDays = 0;

        Map<String, Integer> statusCountMap = new HashMap<>();
        Map<Integer, BigDecimal> actualManDaysMap = new HashMap<>();

        // Pre-calculate actual man-days per project
        for (DailyEffortEntry effort : allEfforts) {
            if (effort.getProject() != null && effort.getManDays() != null) {
                Integer pid = effort.getProject().getProjectId();
                actualManDaysMap.put(pid, actualManDaysMap.getOrDefault(pid, BigDecimal.ZERO).add(effort.getManDays()));
            }
        }

        List<ProjectDashboardResponse.ProjectManDaysChart> chartDataList = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (Project p : allProjects) {
            String status = p.getStatus() != null ? p.getStatus() : "Unknown";
            
            // Count statuses
            statusCountMap.put(status, statusCountMap.getOrDefault(status, 0) + 1);

            if ("Completed".equalsIgnoreCase(status)) {
                completedProjects++;
            } else if ("Active".equalsIgnoreCase(status)) {
                activeProjects++;
            }

            // Calculate overdue
            if (!"Completed".equalsIgnoreCase(status) && p.getEndDate() != null && p.getEndDate().isBefore(today)) {
                overdueProjects++;
                totalOverdueDays += ChronoUnit.DAYS.between(p.getEndDate(), today);
            }

            // Chart data for planned vs actual
            BigDecimal plannedMd = p.getPlannedManDays() != null ? p.getPlannedManDays() : BigDecimal.ZERO;
            BigDecimal actualMd = actualManDaysMap.getOrDefault(p.getProjectId(), BigDecimal.ZERO);
            
            chartDataList.add(new ProjectDashboardResponse.ProjectManDaysChart(
                    p.getProjectName(),
                    plannedMd,
                    actualMd
            ));
        }

        // Calculate project completion rate
        double completionRate = 0.0;
        if (totalProjects > 0) {
            completionRate = ((double) completedProjects / totalProjects) * 100.0;
            // Round to 1 decimal place
            completionRate = Math.round(completionRate * 10.0) / 10.0;
        }

        // Format projects by status
        List<ProjectDashboardResponse.ProjectStatusChart> statusChartList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : statusCountMap.entrySet()) {
            double percentage = 0.0;
            if (totalProjects > 0) {
                percentage = ((double) entry.getValue() / totalProjects) * 100.0;
                percentage = Math.round(percentage * 10.0) / 10.0;
            }
            statusChartList.add(new ProjectDashboardResponse.ProjectStatusChart(
                    entry.getKey(),
                    entry.getValue(),
                    percentage
            ));
        }

        ProjectDashboardResponse response = new ProjectDashboardResponse();
        response.setProjectCompletionRate(completionRate);
        response.setTotalProjects(totalProjects);
        response.setCompletedProjects(completedProjects);
        response.setActiveProjects(activeProjects);
        response.setOverdueProjects(overdueProjects);
        response.setTotalOverdueDays(totalOverdueDays);
        response.setProjectsByStatus(statusChartList);
        response.setPlannedVsActualManDays(chartDataList);

        return response;
    }
}

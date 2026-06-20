package com.hospital.Arogeva.service;

import com.hospital.Arogeva.payload.ProjectDashboardResponse;
import java.time.LocalDate;

public interface ProjectDashboardService {

    ProjectDashboardResponse getProjectManagementDashboard(Integer projectId, String projectManager, LocalDate startDate, LocalDate endDate);

}

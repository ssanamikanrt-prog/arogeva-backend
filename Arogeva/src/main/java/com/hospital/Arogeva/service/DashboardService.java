package com.hospital.Arogeva.service;

import com.hospital.Arogeva.payload.*;

import java.util.List;

public interface DashboardService {

    ExecutiveDashboardResponse getExecutiveDashboard(Integer projectId);

    WeeklyDashboardResponse getWeeklyDashboardData(Integer weekId);

    WeekSummaryWrapperResponse getWeeklySummaryData();

    ResourceSummaryWrapperResponse getResourceSummary();

    ModuleSummaryWrapperResponse getModuleSummary();

    EffortEntryWrapperResponse getAllEffortEntries();

    EffortEntryWrapperResponse getEffortEntriesByResource(Integer resourceId);


}



//
//package com.hospital.Arogeva.service;
//
//import com.hospital.Arogeva.payload.*;
//
//import java.util.List;
//
//public interface DashboardService {
//
//    ExecutiveDashboardResponse getExecutiveDashboard(Integer projectId);
//
//    WeeklyDashboardResponse getWeeklyDashboardData(Integer weekId);
//
//    WeekSummaryWrapperResponse getWeeklySummaryData();
//
//    ResourceSummaryWrapperResponse getResourceSummary();
//
//    ModuleSummaryWrapperResponse getModuleSummary();
//
//    EffortEntryWrapperResponse getAllEffortEntries();
//
//    EffortEntryWrapperResponse getEffortEntriesByResource(Integer resourceId);
//
//
//}




package com.hospital.Arogeva.service;


import com.hospital.Arogeva.payload.*;

import java.util.List;

public interface DashboardService {

    ExecutiveDashboardResponse getExecutiveDashboard(Integer projectId);

    WeeklyDashboardResponse getWeeklyDashboardData(Integer weekId, Integer projectId);

    WeekSummaryWrapperResponse getWeeklySummaryData(Integer projectId);

    ResourceSummaryWrapperResponse getResourceSummary(Integer projectId);

    ModuleSummaryWrapperResponse getModuleSummary(Integer projectId);

    EffortEntryWrapperResponse getAllEffortEntries(Integer projectId);

    EffortEntryWrapperResponse getEffortEntriesByResource(Integer resourceId, Integer projectId);


}







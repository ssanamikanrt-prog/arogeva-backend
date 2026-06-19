


//package com.hospital.Arogeva.service.impl;
//
//import com.hospital.Arogeva.entity.DailyEffortEntry;
//import com.hospital.Arogeva.entity.ProjectWeek;
//import com.hospital.Arogeva.entity.Resource;
//import com.hospital.Arogeva.payload.*;
//import com.hospital.Arogeva.repository.DailyEffortRepository;
//import com.hospital.Arogeva.repository.ModuleRepository;
//import com.hospital.Arogeva.repository.ProjectWeekRepository;
//import com.hospital.Arogeva.repository.ProjectRepository;
//import com.hospital.Arogeva.repository.ResourceRepository;
//import com.hospital.Arogeva.entity.Project;
//import com.hospital.Arogeva.entity.DeveloperMaster;
//import com.hospital.Arogeva.service.DashboardService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//import java.util.stream.Collectors;
//import com.hospital.Arogeva.entity.Module;
//
//import static java.time.temporal.ChronoUnit.DAYS;
//
//@Service
//public class DashboardServiceImpl implements DashboardService {
//
//    @Autowired
//    private DailyEffortRepository dailyEffortRepository;
//
//    @Autowired
//    private ProjectWeekRepository projectWeekRepository;
//
//
//    @Autowired
//    private ResourceRepository resourceRepository;
//
//
//    @Autowired
//    private ModuleRepository moduleRepository;
//
//    @Autowired
//    private ProjectRepository projectRepository;
//
//
//    @Override
//    public ExecutiveDashboardResponse getExecutiveDashboard(Integer projectId) {
//
//        Project project = projectRepository.findById(projectId)
//                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));
//
//        String projectName = project.getProjectName() != null ? project.getProjectName() : "Arogeva V2 Development";
//        String architecture = project.getArchitecture() != null ? project.getArchitecture() : "React + Android + Spring Boot + PostgreSQL";
//        BigDecimal totalBudget = project.getPlannedBudget() != null ? project.getPlannedBudget() : new BigDecimal("350000");
//        BigDecimal plannedManDays = project.getPlannedManDays() != null ? project.getPlannedManDays() : new BigDecimal("70");
//
//        String timelineStr = "45 Calendar Days";
//        if (project.getStartDate() != null && project.getEndDate() != null) {
//            long days = DAYS.between(project.getStartDate(), project.getEndDate());
//            timelineStr = days + " Calendar Days";
//        }
//
//        String rateStr = "₹5,000 / Man-Day";
//        if (plannedManDays.compareTo(BigDecimal.ZERO) > 0) {
//            BigDecimal rate = totalBudget.divide(plannedManDays, 0, RoundingMode.HALF_UP);
//            rateStr = "₹" + rate.toString() + " / Man-Day";
//        }
//
//        List<DailyEffortEntry> allEntries = dailyEffortRepository.findByProjectIdWithDetails(projectId);
//
//        BigDecimal actualManDays = BigDecimal.ZERO;
//        BigDecimal budgetBurnt = BigDecimal.ZERO;
//
//        Map<Integer, BigDecimal> actualCostByDeveloperType = new HashMap<>();
//        Map<Integer, BigDecimal> actualMdByDeveloperType = new HashMap<>();
//
//        for (DailyEffortEntry e : allEntries) {
//            BigDecimal md = e.getManDays() != null ? e.getManDays() : BigDecimal.ZERO;
//            actualManDays = actualManDays.add(md);
//
//            if (e.getResource() != null) {
//                BigDecimal rate = e.getResource().getRatePerDay() != null ? e.getResource().getRatePerDay() : BigDecimal.ZERO;
//                BigDecimal cost = md.multiply(rate);
//                budgetBurnt = budgetBurnt.add(cost);
//
//                if (e.getResource().getDeveloperName() != null) {
//                    Integer devTypeId = e.getResource().getDeveloperName().getDeveloperTypeId();
//                    actualMdByDeveloperType.put(devTypeId, actualMdByDeveloperType.getOrDefault(devTypeId, BigDecimal.ZERO).add(md));
//                    actualCostByDeveloperType.put(devTypeId, actualCostByDeveloperType.getOrDefault(devTypeId, BigDecimal.ZERO).add(cost));
//                }
//            }
//        }
//
//        BigDecimal mdRemaining = plannedManDays.subtract(actualManDays);
//        if (mdRemaining.compareTo(BigDecimal.ZERO) < 0) mdRemaining = BigDecimal.ZERO;
//
//        String burnRate = "0.0%";
//        if (plannedManDays.compareTo(BigDecimal.ZERO) > 0) {
//            BigDecimal p = actualManDays.divide(plannedManDays, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
//            burnRate = p.setScale(1, RoundingMode.HALF_UP).toString() + "%";
//        }
//
//        ExecutiveDashboardResponse.TopLevelMetrics topLevel = new ExecutiveDashboardResponse.TopLevelMetrics(
//                plannedManDays, actualManDays, mdRemaining, budgetBurnt, burnRate
//        );
//
//        ExecutiveDashboardResponse.ProjectDetails pd = new ExecutiveDashboardResponse.ProjectDetails(
//                projectName, architecture, timelineStr, rateStr, totalBudget
//        );
//
//        BigDecimal budgetLeft = totalBudget.subtract(budgetBurnt);
//        if (budgetLeft.compareTo(BigDecimal.ZERO) < 0) budgetLeft = BigDecimal.ZERO;
//
//        ExecutiveDashboardResponse.BudgetToActuals bta = new ExecutiveDashboardResponse.BudgetToActuals(
//                totalBudget, budgetBurnt, budgetLeft, "Daily Entries", "On Track"
//        );
//
//        List<Resource> resources = resourceRepository.findByProjectIdWithDetails(projectId);
//        Map<Integer, ExecutiveDashboardResponse.TeamOverviewItem> teamMap = new HashMap<>();
//
//        for (Resource r : resources) {
//            if (r.getDeveloperName() != null) {
//                DeveloperMaster dm = r.getDeveloperName();
//                Integer dtId = dm.getDeveloperTypeId();
//
//                if (teamMap.containsKey(dtId)) {
//                    ExecutiveDashboardResponse.TeamOverviewItem item = teamMap.get(dtId);
//                    item.setPlanned(item.getPlanned().add(r.getPlannedManDays() != null ? r.getPlannedManDays() : BigDecimal.ZERO));
//                } else {
//                    ExecutiveDashboardResponse.TeamOverviewItem item = new ExecutiveDashboardResponse.TeamOverviewItem();
//                    item.setInitials(getInitials(dm.getDeveloperName()));
//                    item.setRoleName(dm.getDeveloperName());
//                    item.setDescription(dm.getDescription() != null ? dm.getDescription() : dm.getDeveloperName());
//                    item.setPlanned(r.getPlannedManDays() != null ? r.getPlannedManDays() : BigDecimal.ZERO);
//                    item.setActual(BigDecimal.ZERO);
//                    item.setCost(BigDecimal.ZERO);
//                    item.setStatus("On Track");
//                    teamMap.put(dtId, item);
//                }
//            }
//        }
//
//        List<ExecutiveDashboardResponse.TeamOverviewItem> teamOverview = new ArrayList<>();
//
//        for (Map.Entry<Integer, ExecutiveDashboardResponse.TeamOverviewItem> entry : teamMap.entrySet()) {
//            Integer dtId = entry.getKey();
//            ExecutiveDashboardResponse.TeamOverviewItem item = entry.getValue();
//
//            BigDecimal act = actualMdByDeveloperType.getOrDefault(dtId, BigDecimal.ZERO);
//            BigDecimal cost = actualCostByDeveloperType.getOrDefault(dtId, BigDecimal.ZERO);
//
//            item.setActual(act);
//            item.setCost(cost);
//
//            if (act.compareTo(item.getPlanned()) > 0) {
//                item.setStatus("Overrun");
//            } else if (act.compareTo(item.getPlanned().multiply(new BigDecimal("0.85"))) >= 0) {
//                item.setStatus("Watch");
//            }
//
//            teamOverview.add(item);
//        }
//
//        return new ExecutiveDashboardResponse(topLevel, pd, bta, teamOverview);
//    }
//
//
//
//    private String getInitials(String name) {
//        if (name == null || name.isEmpty()) return "N/A";
//        String[] words = name.split("\\s+");
//        if (words.length == 1) {
//            return words[0].substring(0, Math.min(2, words[0].length())).toUpperCase();
//        } else if (words.length >= 2) {
//            if (name.contains("+")) {
//                return name.substring(0, 1).toUpperCase() + "+";
//            }
//            return (words[0].substring(0, 1) + words[1].substring(0, 1)).toUpperCase();
//        }
//        return "N/A";
//    }
//
//
//
//    @Override
//    public WeeklyDashboardResponse getWeeklyDashboardData(Integer weekId) {
//        List<DailyEffortEntry> entries = dailyEffortRepository.findByWeekIdWithDetails(weekId);
//
//        BigDecimal totalEffort = BigDecimal.ZERO;
//        BigDecimal totalHours = BigDecimal.ZERO;
//        BigDecimal totalCost = BigDecimal.ZERO;
//        int entriesCount = entries.size();
//
//        Map<String, List<DailyEffortEntry>> groupedEntries = new HashMap<>();
//
//        for (DailyEffortEntry entry : entries) {
//            BigDecimal md = entry.getManDays() != null ? entry.getManDays() : BigDecimal.ZERO;
//            BigDecimal hrs = entry.getHoursWorked() != null ? entry.getHoursWorked() : BigDecimal.ZERO;
//
//            totalEffort = totalEffort.add(md);
//            totalHours = totalHours.add(hrs);
//
//            Resource resource = entry.getResource();
//            if (resource != null && resource.getRatePerDay() != null) {
//                totalCost = totalCost.add(md.multiply(resource.getRatePerDay()));
//            }
//
//            Integer resId = resource != null ? resource.getResourceId() : 0;
//            Integer modId = entry.getModule() != null ? entry.getModule().getModuleId() : 0;
//            String key = resId + "_" + modId;
//
//            groupedEntries.computeIfAbsent(key, k -> new ArrayList<>()).add(entry);
//        }
//
//        BigDecimal dailyAverage = BigDecimal.ZERO;
//        if (totalEffort.compareTo(BigDecimal.ZERO) > 0) {
//            dailyAverage = totalEffort.divide(new BigDecimal("6"), 2, RoundingMode.HALF_UP);
//        }
//
//        List<WeeklyDashboardResponse.ResourceEffortRow> rows = new ArrayList<>();
//        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
//        for (Map.Entry<String, List<DailyEffortEntry>> group : groupedEntries.entrySet()) {
//            List<DailyEffortEntry> groupList = group.getValue();
//            DailyEffortEntry firstEntry = groupList.get(0);
//            Resource res = firstEntry.getResource();
//
//            String resourceName = res != null && res.getUser() != null ? res.getUser().getFullName() : "Unknown";
//            String role = res != null && res.getDeveloperName() != null ? res.getDeveloperName().getDeveloperName() : "Unknown";
//            String moduleName = firstEntry.getModule() != null ? firstEntry.getModule().getModuleName() : "N/A";
//            Integer resourceId = res != null ? res.getResourceId() : null;
//            Integer moduleId = firstEntry.getModule() != null ? firstEntry.getModule().getModuleId() : null;
//
//            BigDecimal weekMd = BigDecimal.ZERO;
//            BigDecimal weekCost = BigDecimal.ZERO;
//
//            List<WeeklyDashboardResponse.DailyEffortData> dailyData = new ArrayList<>();
//
//            Map<LocalDate, BigDecimal> dailyMap = new HashMap<>();
//            for (DailyEffortEntry e : groupList) {
//                if (e.getWorkDate() != null) {
//                    BigDecimal mDays = e.getManDays() != null ? e.getManDays() : BigDecimal.ZERO;
//                    dailyMap.put(e.getWorkDate(), dailyMap.getOrDefault(e.getWorkDate(), BigDecimal.ZERO).add(mDays));
//
//                    weekMd = weekMd.add(mDays);
//                    if (res != null && res.getRatePerDay() != null) {
//                        weekCost = weekCost.add(mDays.multiply(res.getRatePerDay()));
//                    }
//                }
//            }
//
//            ProjectWeek pw = firstEntry.getWeek();
//            if (pw != null && pw.getStartDate() != null && pw.getEndDate() != null) {
//                LocalDate current = pw.getStartDate();
//                while (!current.isAfter(pw.getEndDate())) {
//                    BigDecimal manDays = dailyMap.getOrDefault(current, BigDecimal.ZERO);
//                    String dayOfWeek = current.getDayOfWeek().toString().substring(0, 3);
//                    dayOfWeek = dayOfWeek.substring(0, 1).toUpperCase() + dayOfWeek.substring(1).toLowerCase();
//                    dailyData.add(new WeeklyDashboardResponse.DailyEffortData(current.format(dateFormatter), dayOfWeek, manDays));
//                    current = current.plusDays(1);
//                }
//            } else {
//                dailyMap.keySet().stream().sorted().forEach(date -> {
//                    String dayOfWeek = date.getDayOfWeek().toString().substring(0, 3);
//                    dayOfWeek = dayOfWeek.substring(0, 1).toUpperCase() + dayOfWeek.substring(1).toLowerCase();
//                    dailyData.add(new WeeklyDashboardResponse.DailyEffortData(date.format(dateFormatter), dayOfWeek, dailyMap.get(date)));
//                });
//            }
//
//            rows.add(new WeeklyDashboardResponse.ResourceEffortRow(
//                    resourceId, moduleId, resourceName, role, moduleName, dailyData, weekMd, weekCost
//            ));
//        }
//
//        Map<String, WeeklyDashboardResponse.DailyEffortData> totalDailyDataMap = new LinkedHashMap<>();
//        for (WeeklyDashboardResponse.ResourceEffortRow row : rows) {
//            for (WeeklyDashboardResponse.DailyEffortData dd : row.getDailyData()) {
//                WeeklyDashboardResponse.DailyEffortData currentTotal = totalDailyDataMap.getOrDefault(dd.getDate(),
//                        new WeeklyDashboardResponse.DailyEffortData(dd.getDate(), dd.getDayOfWeek(), BigDecimal.ZERO));
//                currentTotal.setManDays(currentTotal.getManDays().add(dd.getManDays()));
//                totalDailyDataMap.put(dd.getDate(), currentTotal);
//            }
//        }
//
//        WeeklyDashboardResponse.ResourceEffortRow dailyTotal = new WeeklyDashboardResponse.ResourceEffortRow(
//                0, 0, "DAILY TOTAL", "", "", new ArrayList<>(totalDailyDataMap.values()), totalEffort, totalCost
//        );
//
//        return new WeeklyDashboardResponse(totalEffort, totalHours, totalCost, dailyAverage, entriesCount, rows, dailyTotal);
//    }
//
//    @Override
//    public WeekSummaryWrapperResponse getWeeklySummaryData() {
//        List<ProjectWeek> allWeeks = projectWeekRepository.findAll();
//        List<DailyEffortEntry> allEntries = dailyEffortRepository.findAllWithDetails();
//
//        Map<Integer, List<DailyEffortEntry>> entriesByWeek = allEntries.stream()
//                .filter(e -> e.getWeek() != null)
//                .collect(Collectors.groupingBy(e -> e.getWeek().getWeekId()));
//
//        List<WeekSummaryResponse> summaries = new ArrayList<>();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
//        BigDecimal totalHoursSum = BigDecimal.ZERO;
//        BigDecimal totalManDaysSum = BigDecimal.ZERO;
//        BigDecimal totalCostSum = BigDecimal.ZERO;
//        Integer totalEntriesSum = 0;
//
//        for (ProjectWeek week : allWeeks) {
//            List<DailyEffortEntry> weekEntries = entriesByWeek.getOrDefault(week.getWeekId(), new ArrayList<>());
//
//            BigDecimal hours = BigDecimal.ZERO;
//            BigDecimal manDays = BigDecimal.ZERO;
//            BigDecimal cost = BigDecimal.ZERO;
//
//            for (DailyEffortEntry e : weekEntries) {
//                BigDecimal h = e.getHoursWorked() != null ? e.getHoursWorked() : BigDecimal.ZERO;
//                BigDecimal md = e.getManDays() != null ? e.getManDays() : BigDecimal.ZERO;
//                hours = hours.add(h);
//                manDays = manDays.add(md);
//
//                if (e.getResource() != null && e.getResource().getRatePerDay() != null) {
//                    cost = cost.add(md.multiply(e.getResource().getRatePerDay()));
//                }
//            }
//
//            String dateRange = (week.getStartDate() != null ? week.getStartDate().format(formatter) : "")
//                    + " to "
//                    + (week.getEndDate() != null ? week.getEndDate().format(formatter) : "");
//
//            summaries.add(new WeekSummaryResponse(
//                    week.getWeekId(),
//                    week.getWeekName(),
//                    dateRange,
//                    hours,
//                    manDays,
//                    cost,
//                    weekEntries.size()
//            ));
//
//            totalHoursSum = totalHoursSum.add(hours);
//            totalManDaysSum = totalManDaysSum.add(manDays);
//            totalCostSum = totalCostSum.add(cost);
//            totalEntriesSum += weekEntries.size();
//        }
//
//        summaries.sort(Comparator.comparing(WeekSummaryResponse::getWeekId));
//
//        WeekSummaryWrapperResponse.WeekTotalMetrics totalMetrics = new WeekSummaryWrapperResponse.WeekTotalMetrics(
//                totalHoursSum, totalManDaysSum, totalCostSum, totalEntriesSum
//        );
//
//        return new WeekSummaryWrapperResponse(summaries, totalMetrics);
//    }
//
//
//    @Override
//    public ResourceSummaryWrapperResponse getResourceSummary() {
//        List<Resource> resources = resourceRepository.findAllWithDetails();
//        List<DailyEffortEntry> allEntries = dailyEffortRepository.findAll();
//
//        Map<Integer, BigDecimal> actualMdMap = new HashMap<>();
//        for (DailyEffortEntry e : allEntries) {
//            if (e.getResource() != null) {
//                Integer rid = e.getResource().getResourceId();
//                BigDecimal currentMd = actualMdMap.getOrDefault(rid, BigDecimal.ZERO);
//                BigDecimal mdToAdd = e.getManDays() != null ? e.getManDays() : BigDecimal.ZERO;
//                actualMdMap.put(rid, currentMd.add(mdToAdd));
//            }
//        }
//
//        List<ResourceSummaryResponse> result = new ArrayList<>();
//        BigDecimal totalPlanned = BigDecimal.ZERO;
//        BigDecimal totalActual = BigDecimal.ZERO;
//        BigDecimal totalLeft = BigDecimal.ZERO;
//        BigDecimal totalCost = BigDecimal.ZERO;
//
//        for (Resource r : resources) {
//            String name = r.getUser() != null ? r.getUser().getFullName() : "Unknown";
//            String role = r.getDeveloperName() != null ? r.getDeveloperName().getDeveloperName() : "Unknown";
//            String level = r.getExperienceLevel() != null ? r.getExperienceLevel() : "-";
//            String moduleName = r.getModule() != null ? r.getModule().getModuleName() : "-";
//
//            BigDecimal rate = r.getRatePerDay() != null ? r.getRatePerDay() : BigDecimal.ZERO;
//            BigDecimal planned = r.getPlannedManDays() != null ? r.getPlannedManDays() : BigDecimal.ZERO;
//            BigDecimal actual = actualMdMap.getOrDefault(r.getResourceId(), BigDecimal.ZERO);
//
//            BigDecimal left = planned.subtract(actual);
//            BigDecimal cost = actual.multiply(rate);
//
//            totalPlanned = totalPlanned.add(planned);
//            totalActual = totalActual.add(actual);
//            totalLeft = totalLeft.add(left);
//            totalCost = totalCost.add(cost);
//
//            String status = "On Track";
//            if (actual.compareTo(planned) > 0) {
//                status = "Overrun";
//            } else if (actual.compareTo(planned.multiply(new BigDecimal("0.85"))) >= 0) {
//                status = "Watch";
//            }
//
//            result.add(new ResourceSummaryResponse(
//                    r.getResourceId(), name, role, level, moduleName, rate, planned, actual, left, cost, status
//            ));
//        }
//
//        ResourceSummaryWrapperResponse.TotalMetrics totalMetrics = new ResourceSummaryWrapperResponse.TotalMetrics(
//                totalPlanned, totalActual, totalLeft, totalCost
//        );
//
//        return new ResourceSummaryWrapperResponse(result, totalMetrics);
//    }
//
//
//
//    @Override
//    public ModuleSummaryWrapperResponse getModuleSummary() {
//        List<Module> modules = moduleRepository.findAll();
//        List<DailyEffortEntry> allEntries = dailyEffortRepository.findAll();
//
//        Map<Integer, BigDecimal> actualMdMap = new HashMap<>();
//        for (DailyEffortEntry e : allEntries) {
//            if (e.getModule() != null) {
//                Integer mid = e.getModule().getModuleId();
//                BigDecimal currentMd = actualMdMap.getOrDefault(mid, BigDecimal.ZERO);
//                BigDecimal mdToAdd = e.getManDays() != null ? e.getManDays() : BigDecimal.ZERO;
//                actualMdMap.put(mid, currentMd.add(mdToAdd));
//            }
//        }
//
//        List<ModuleSummaryResponse> result = new ArrayList<>();
//        BigDecimal totalPlannedMd = BigDecimal.ZERO;
//        BigDecimal totalPlannedCost = BigDecimal.ZERO;
//        BigDecimal totalActualMd = BigDecimal.ZERO;
//        for (Module m : modules) {
//            BigDecimal plannedMd = m.getPlannedManDays() != null ? m.getPlannedManDays() : BigDecimal.ZERO;
//            BigDecimal plannedCost = m.getPlannedCost() != null ? m.getPlannedCost() : BigDecimal.ZERO;
//            BigDecimal actualMd = actualMdMap.getOrDefault(m.getModuleId(), BigDecimal.ZERO);
//
//            totalPlannedMd = totalPlannedMd.add(plannedMd);
//            totalPlannedCost = totalPlannedCost.add(plannedCost);
//            totalActualMd = totalActualMd.add(actualMd);
//
//            String percent = "0.0%";
//            if (plannedMd.compareTo(BigDecimal.ZERO) > 0) {
//                BigDecimal p = actualMd.divide(plannedMd, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
//                percent = p.setScale(1, RoundingMode.HALF_UP).toString() + "%";
//            }
//
//            result.add(new ModuleSummaryResponse(
//                    m.getModuleId(),
//                    m.getModuleName(),
//                    m.getScopeDescription(),
//                    plannedMd,
//                    plannedCost,
//                    actualMd,
//                    percent
//            ));
//        }
//
//        String totalPercent = "0.0%";
//        if (totalPlannedMd.compareTo(BigDecimal.ZERO) > 0) {
//            BigDecimal p = totalActualMd.divide(totalPlannedMd, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
//            totalPercent = p.setScale(1, RoundingMode.HALF_UP).toString() + "%";
//        }
//
//        ModuleSummaryWrapperResponse.ModuleTotalMetrics totalMetrics = new ModuleSummaryWrapperResponse.ModuleTotalMetrics(
//                totalPlannedMd, totalPlannedCost, totalActualMd, totalPercent
//        );
//
//        return new ModuleSummaryWrapperResponse(result, totalMetrics);
//    }
//
//    @Override
//    public EffortEntryWrapperResponse getAllEffortEntries() {
//        List<DailyEffortEntry> allEntries = dailyEffortRepository.findAllWithDetails();
//
//        List<EffortEntryResponse> entries = allEntries.stream().map(e -> {
//            String resourceName = (e.getResource() != null && e.getResource().getUser() != null)
//                    ? e.getResource().getUser().getFullName() : "-";
//            String weekName = e.getWeek() != null ? e.getWeek().getWeekName() : "-";
//            String moduleName = e.getModule() != null ? e.getModule().getModuleName() : "-";
//            String activityName = e.getActivity() != null ? e.getActivity().getActivityName() : "-";
//            String status = e.getStatus() != null ? e.getStatus().getStatusName() : "-";
//
//            return new EffortEntryResponse(
//                    e.getEntryId(),
//                    resourceName,
//                    e.getWorkDate(),
//                    weekName,
//                    moduleName,
//                    activityName,
//                    e.getHoursWorked() != null ? e.getHoursWorked() : BigDecimal.ZERO,
//                    e.getManDays() != null ? e.getManDays() : BigDecimal.ZERO,
//                    status,
//                    e.getWorkDescription()
//            );
//        }).collect(Collectors.toList());
//
//        BigDecimal totalHours = entries.stream().map(EffortEntryResponse::getHours).reduce(BigDecimal.ZERO, BigDecimal::add);
//        BigDecimal totalMd = entries.stream().map(EffortEntryResponse::getManDays).reduce(BigDecimal.ZERO, BigDecimal::add);
//
//        return new EffortEntryWrapperResponse(entries, new EffortEntryWrapperResponse.EffortTotalMetrics(totalHours, totalMd));
//    }
//
//    @Override
//    public EffortEntryWrapperResponse getEffortEntriesByResource(Integer resourceId) {
//        List<DailyEffortEntry> entryList = dailyEffortRepository.findByResourceIdWithDetails(resourceId);
//
//        List<EffortEntryResponse> entries = entryList.stream().map(e -> {
//            String resourceName = (e.getResource() != null && e.getResource().getUser() != null)
//                    ? e.getResource().getUser().getFullName() : "-";
//            String weekName = e.getWeek() != null ? e.getWeek().getWeekName() : "-";
//            String moduleName = e.getModule() != null ? e.getModule().getModuleName() : "-";
//            String activityName = e.getActivity() != null ? e.getActivity().getActivityName() : "-";
//            String status = e.getStatus() != null ? e.getStatus().getStatusName() : "-";
//
//            return new EffortEntryResponse(
//                    e.getEntryId(),
//                    resourceName,
//                    e.getWorkDate(),
//                    weekName,
//                    moduleName,
//                    activityName,
//                    e.getHoursWorked() != null ? e.getHoursWorked() : BigDecimal.ZERO,
//                    e.getManDays() != null ? e.getManDays() : BigDecimal.ZERO,
//                    status,
//                    e.getWorkDescription()
//            );
//        }).collect(Collectors.toList());
//
//        BigDecimal totalHours = entries.stream().map(EffortEntryResponse::getHours).reduce(BigDecimal.ZERO, BigDecimal::add);
//        BigDecimal totalMd = entries.stream().map(EffortEntryResponse::getManDays).reduce(BigDecimal.ZERO, BigDecimal::add);
//
//        return new EffortEntryWrapperResponse(entries, new EffortEntryWrapperResponse.EffortTotalMetrics(totalHours, totalMd));
//    }
//}





package com.hospital.Arogeva.service.impl;

import com.hospital.Arogeva.entity.DailyEffortEntry;
import com.hospital.Arogeva.entity.ProjectWeek;
import com.hospital.Arogeva.entity.Resource;
import com.hospital.Arogeva.payload.*;
import com.hospital.Arogeva.repository.DailyEffortRepository;
import com.hospital.Arogeva.repository.ModuleRepository;
import com.hospital.Arogeva.repository.ProjectWeekRepository;
import com.hospital.Arogeva.repository.ProjectRepository;
import com.hospital.Arogeva.repository.ResourceRepository;
import com.hospital.Arogeva.entity.Project;
import com.hospital.Arogeva.entity.DeveloperMaster;
import com.hospital.Arogeva.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

        import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import com.hospital.Arogeva.entity.Module;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private DailyEffortRepository dailyEffortRepository;

    @Autowired
    private ProjectWeekRepository projectWeekRepository;


    @Autowired
    private ResourceRepository resourceRepository;


    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private ProjectRepository projectRepository;


    @Override
    public ExecutiveDashboardResponse getExecutiveDashboard(Integer projectId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));

        String projectName = project.getProjectName() != null ? project.getProjectName() : "Arogeva V2 Development";
        String architecture = project.getArchitecture() != null ? project.getArchitecture() : "React + Android + Spring Boot + PostgreSQL";
        BigDecimal totalBudget = project.getPlannedBudget() != null ? project.getPlannedBudget() : new BigDecimal("350000");
        BigDecimal plannedManDays = project.getPlannedManDays() != null ? project.getPlannedManDays() : new BigDecimal("70");

        String timelineStr = "45 Calendar Days";
        if (project.getStartDate() != null && project.getEndDate() != null) {
            long days = DAYS.between(project.getStartDate(), project.getEndDate());
            timelineStr = days + " Calendar Days";
        }

        String rateStr = "₹5,000 / Man-Day";
        if (plannedManDays.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal rate = totalBudget.divide(plannedManDays, 0, RoundingMode.HALF_UP);
            rateStr = "₹" + rate.toString() + " / Man-Day";
        }

        List<DailyEffortEntry> allEntries = dailyEffortRepository.findByProjectIdWithDetails(projectId);

        BigDecimal actualManDays = BigDecimal.ZERO;
        BigDecimal budgetBurnt = BigDecimal.ZERO;

        Map<Integer, BigDecimal> actualCostByDeveloperType = new HashMap<>();
        Map<Integer, BigDecimal> actualMdByDeveloperType = new HashMap<>();

        for (DailyEffortEntry e : allEntries) {
            BigDecimal md = e.getManDays() != null ? e.getManDays() : BigDecimal.ZERO;
            actualManDays = actualManDays.add(md);

            if (e.getResource() != null) {
                BigDecimal rate = e.getResource().getRatePerDay() != null ? e.getResource().getRatePerDay() : BigDecimal.ZERO;
                BigDecimal cost = md.multiply(rate);
                budgetBurnt = budgetBurnt.add(cost);

                if (e.getResource().getDeveloperName() != null) {
                    Integer devTypeId = e.getResource().getDeveloperName().getDeveloperTypeId();
                    actualMdByDeveloperType.put(devTypeId, actualMdByDeveloperType.getOrDefault(devTypeId, BigDecimal.ZERO).add(md));
                    actualCostByDeveloperType.put(devTypeId, actualCostByDeveloperType.getOrDefault(devTypeId, BigDecimal.ZERO).add(cost));
                }
            }
        }

        BigDecimal mdRemaining = plannedManDays.subtract(actualManDays);
        if (mdRemaining.compareTo(BigDecimal.ZERO) < 0) mdRemaining = BigDecimal.ZERO;

        String burnRate = "0.0%";
        if (plannedManDays.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal p = actualManDays.divide(plannedManDays, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
            burnRate = p.setScale(1, RoundingMode.HALF_UP).toString() + "%";
        }

        ExecutiveDashboardResponse.TopLevelMetrics topLevel = new ExecutiveDashboardResponse.TopLevelMetrics(
                plannedManDays, actualManDays, mdRemaining, budgetBurnt, burnRate
        );

        ExecutiveDashboardResponse.ProjectDetails pd = new ExecutiveDashboardResponse.ProjectDetails(
                projectName, architecture, timelineStr, rateStr, totalBudget
        );

        BigDecimal budgetLeft = totalBudget.subtract(budgetBurnt);
        if (budgetLeft.compareTo(BigDecimal.ZERO) < 0) budgetLeft = BigDecimal.ZERO;

        ExecutiveDashboardResponse.BudgetToActuals bta = new ExecutiveDashboardResponse.BudgetToActuals(
                totalBudget, budgetBurnt, budgetLeft, "Daily Entries", "On Track"
        );

        List<Resource> resources = resourceRepository.findByProjectIdWithDetails(projectId);
        Map<Integer, ExecutiveDashboardResponse.TeamOverviewItem> teamMap = new HashMap<>();

        for (Resource r : resources) {
            if (r.getDeveloperName() != null) {
                DeveloperMaster dm = r.getDeveloperName();
                Integer dtId = dm.getDeveloperTypeId();

                if (teamMap.containsKey(dtId)) {
                    ExecutiveDashboardResponse.TeamOverviewItem item = teamMap.get(dtId);
                    item.setPlanned(item.getPlanned().add(r.getPlannedManDays() != null ? r.getPlannedManDays() : BigDecimal.ZERO));
                } else {
                    ExecutiveDashboardResponse.TeamOverviewItem item = new ExecutiveDashboardResponse.TeamOverviewItem();
                    item.setInitials(getInitials(dm.getDeveloperName()));
                    item.setRoleName(dm.getDeveloperName());
                    item.setDescription(dm.getDescription() != null ? dm.getDescription() : dm.getDeveloperName());
                    item.setPlanned(r.getPlannedManDays() != null ? r.getPlannedManDays() : BigDecimal.ZERO);
                    item.setActual(BigDecimal.ZERO);
                    item.setCost(BigDecimal.ZERO);
                    item.setStatus("On Track");
                    teamMap.put(dtId, item);
                }
            }
        }

        List<ExecutiveDashboardResponse.TeamOverviewItem> teamOverview = new ArrayList<>();

        for (Map.Entry<Integer, ExecutiveDashboardResponse.TeamOverviewItem> entry : teamMap.entrySet()) {
            Integer dtId = entry.getKey();
            ExecutiveDashboardResponse.TeamOverviewItem item = entry.getValue();

            BigDecimal act = actualMdByDeveloperType.getOrDefault(dtId, BigDecimal.ZERO);
            BigDecimal cost = actualCostByDeveloperType.getOrDefault(dtId, BigDecimal.ZERO);

            item.setActual(act);
            item.setCost(cost);

            if (act.compareTo(item.getPlanned()) > 0) {
                item.setStatus("Overrun");
            } else if (act.compareTo(item.getPlanned().multiply(new BigDecimal("0.85"))) >= 0) {
                item.setStatus("Watch");
            }

            teamOverview.add(item);
        }

        return new ExecutiveDashboardResponse(topLevel, pd, bta, teamOverview);
    }



    private String getInitials(String name) {
        if (name == null || name.isEmpty()) return "N/A";
        String[] words = name.split("\\s+");
        if (words.length == 1) {
            return words[0].substring(0, Math.min(2, words[0].length())).toUpperCase();
        } else if (words.length >= 2) {
            if (name.contains("+")) {
                return name.substring(0, 1).toUpperCase() + "+";
            }
            return (words[0].substring(0, 1) + words[1].substring(0, 1)).toUpperCase();
        }
        return "N/A";
    }



    @Override
    public WeeklyDashboardResponse getWeeklyDashboardData(Integer weekId, Integer projectId) {
        List<DailyEffortEntry> entries;
        if (projectId != null) {
            entries = dailyEffortRepository.findByWeekIdAndProjectIdWithDetails(weekId, projectId);
        } else {
            entries = dailyEffortRepository.findByWeekIdWithDetails(weekId);
        }

        BigDecimal totalEffort = BigDecimal.ZERO;
        BigDecimal totalHours = BigDecimal.ZERO;
        BigDecimal totalCost = BigDecimal.ZERO;
        int entriesCount = entries.size();

        Map<String, List<DailyEffortEntry>> groupedEntries = new HashMap<>();

        for (DailyEffortEntry entry : entries) {
            BigDecimal md = entry.getManDays() != null ? entry.getManDays() : BigDecimal.ZERO;
            BigDecimal hrs = entry.getHoursWorked() != null ? entry.getHoursWorked() : BigDecimal.ZERO;

            totalEffort = totalEffort.add(md);
            totalHours = totalHours.add(hrs);

            Resource resource = entry.getResource();
            if (resource != null && resource.getRatePerDay() != null) {
                totalCost = totalCost.add(md.multiply(resource.getRatePerDay()));
            }

            Integer resId = resource != null ? resource.getResourceId() : 0;
            Integer modId = entry.getModule() != null ? entry.getModule().getModuleId() : 0;
            String key = resId + "_" + modId;

            groupedEntries.computeIfAbsent(key, k -> new ArrayList<>()).add(entry);
        }

        BigDecimal dailyAverage = BigDecimal.ZERO;
        if (totalEffort.compareTo(BigDecimal.ZERO) > 0) {
            dailyAverage = totalEffort.divide(new BigDecimal("6"), 2, RoundingMode.HALF_UP);
        }

        List<WeeklyDashboardResponse.ResourceEffortRow> rows = new ArrayList<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Map.Entry<String, List<DailyEffortEntry>> group : groupedEntries.entrySet()) {
            List<DailyEffortEntry> groupList = group.getValue();
            DailyEffortEntry firstEntry = groupList.get(0);
            Resource res = firstEntry.getResource();

            String resourceName = res != null && res.getUser() != null ? res.getUser().getFullName() : "Unknown";
            String role = res != null && res.getDeveloperName() != null ? res.getDeveloperName().getDeveloperName() : "Unknown";
            String moduleName = firstEntry.getModule() != null ? firstEntry.getModule().getModuleName() : "N/A";
            Integer resourceId = res != null ? res.getResourceId() : null;
            Integer moduleId = firstEntry.getModule() != null ? firstEntry.getModule().getModuleId() : null;

            BigDecimal weekMd = BigDecimal.ZERO;
            BigDecimal weekCost = BigDecimal.ZERO;

            List<WeeklyDashboardResponse.DailyEffortData> dailyData = new ArrayList<>();

            Map<LocalDate, BigDecimal> dailyMap = new HashMap<>();
            for (DailyEffortEntry e : groupList) {
                if (e.getWorkDate() != null) {
                    BigDecimal mDays = e.getManDays() != null ? e.getManDays() : BigDecimal.ZERO;
                    dailyMap.put(e.getWorkDate(), dailyMap.getOrDefault(e.getWorkDate(), BigDecimal.ZERO).add(mDays));

                    weekMd = weekMd.add(mDays);
                    if (res != null && res.getRatePerDay() != null) {
                        weekCost = weekCost.add(mDays.multiply(res.getRatePerDay()));
                    }
                }
            }

            ProjectWeek pw = firstEntry.getWeek();
            if (pw != null && pw.getStartDate() != null && pw.getEndDate() != null) {
                LocalDate current = pw.getStartDate();
                while (!current.isAfter(pw.getEndDate())) {
                    BigDecimal manDays = dailyMap.getOrDefault(current, BigDecimal.ZERO);
                    String dayOfWeek = current.getDayOfWeek().toString().substring(0, 3);
                    dayOfWeek = dayOfWeek.substring(0, 1).toUpperCase() + dayOfWeek.substring(1).toLowerCase();
                    dailyData.add(new WeeklyDashboardResponse.DailyEffortData(current.format(dateFormatter), dayOfWeek, manDays));
                    current = current.plusDays(1);
                }
            } else {
                 dailyMap.keySet().stream().sorted().forEach(date -> {
                    String dayOfWeek = date.getDayOfWeek().toString().substring(0, 3);
                    dayOfWeek = dayOfWeek.substring(0, 1).toUpperCase() + dayOfWeek.substring(1).toLowerCase();
                    dailyData.add(new WeeklyDashboardResponse.DailyEffortData(date.format(dateFormatter), dayOfWeek, dailyMap.get(date)));
                 });
            }

            rows.add(new WeeklyDashboardResponse.ResourceEffortRow(
                    resourceId, moduleId, resourceName, role, moduleName, dailyData, weekMd, weekCost
            ));
        }

        Map<String, WeeklyDashboardResponse.DailyEffortData> totalDailyDataMap = new LinkedHashMap<>();
        for (WeeklyDashboardResponse.ResourceEffortRow row : rows) {
            for (WeeklyDashboardResponse.DailyEffortData dd : row.getDailyData()) {
                WeeklyDashboardResponse.DailyEffortData currentTotal = totalDailyDataMap.getOrDefault(dd.getDate(),
                        new WeeklyDashboardResponse.DailyEffortData(dd.getDate(), dd.getDayOfWeek(), BigDecimal.ZERO));
                currentTotal.setManDays(currentTotal.getManDays().add(dd.getManDays()));
                totalDailyDataMap.put(dd.getDate(), currentTotal);
            }
        }

        WeeklyDashboardResponse.ResourceEffortRow dailyTotal = new WeeklyDashboardResponse.ResourceEffortRow(
                0, 0, "DAILY TOTAL", "", "", new ArrayList<>(totalDailyDataMap.values()), totalEffort, totalCost
        );

        return new WeeklyDashboardResponse(totalEffort, totalHours, totalCost, dailyAverage, entriesCount, rows, dailyTotal);
    }

    @Override
    public WeekSummaryWrapperResponse getWeeklySummaryData(Integer projectId) {
        List<ProjectWeek> allWeeks;
        List<DailyEffortEntry> allEntries;

        if (projectId != null) {
            allWeeks = projectWeekRepository.findByProject_ProjectId(projectId);
            allEntries = dailyEffortRepository.findByProjectIdWithDetails(projectId);
        } else {
            allWeeks = projectWeekRepository.findAll();
            allEntries = dailyEffortRepository.findAllWithDetails();
        }

        Map<Integer, List<DailyEffortEntry>> entriesByWeek = allEntries.stream()
                .filter(e -> e.getWeek() != null)
                .collect(Collectors.groupingBy(e -> e.getWeek().getWeekId()));

        List<WeekSummaryResponse> summaries = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        BigDecimal totalHoursSum = BigDecimal.ZERO;
        BigDecimal totalManDaysSum = BigDecimal.ZERO;
        BigDecimal totalCostSum = BigDecimal.ZERO;
        Integer totalEntriesSum = 0;

        for (ProjectWeek week : allWeeks) {
            List<DailyEffortEntry> weekEntries = entriesByWeek.getOrDefault(week.getWeekId(), new ArrayList<>());

            BigDecimal hours = BigDecimal.ZERO;
            BigDecimal manDays = BigDecimal.ZERO;
            BigDecimal cost = BigDecimal.ZERO;

            for (DailyEffortEntry e : weekEntries) {
                BigDecimal h = e.getHoursWorked() != null ? e.getHoursWorked() : BigDecimal.ZERO;
                BigDecimal md = e.getManDays() != null ? e.getManDays() : BigDecimal.ZERO;
                hours = hours.add(h);
                manDays = manDays.add(md);

                if (e.getResource() != null && e.getResource().getRatePerDay() != null) {
                    cost = cost.add(md.multiply(e.getResource().getRatePerDay()));
                }
            }

            String dateRange = (week.getStartDate() != null ? week.getStartDate().format(formatter) : "")
                    + " to "
                    + (week.getEndDate() != null ? week.getEndDate().format(formatter) : "");

            summaries.add(new WeekSummaryResponse(
                    week.getWeekId(),
                    week.getWeekName(),
                    dateRange,
                    hours,
                    manDays,
                    cost,
                    weekEntries.size()
            ));

            totalHoursSum = totalHoursSum.add(hours);
            totalManDaysSum = totalManDaysSum.add(manDays);
            totalCostSum = totalCostSum.add(cost);
            totalEntriesSum += weekEntries.size();
        }

        summaries.sort(Comparator.comparing(WeekSummaryResponse::getWeekId));

        WeekSummaryWrapperResponse.WeekTotalMetrics totalMetrics = new WeekSummaryWrapperResponse.WeekTotalMetrics(
                totalHoursSum, totalManDaysSum, totalCostSum, totalEntriesSum
        );

        return new WeekSummaryWrapperResponse(summaries, totalMetrics);
    }


    @Override
    public ResourceSummaryWrapperResponse getResourceSummary(Integer projectId) {
        List<Resource> resources;
        List<DailyEffortEntry> allEntries;

        if (projectId != null) {
            resources = resourceRepository.findByProjectIdWithDetails(projectId);
            allEntries = dailyEffortRepository.findByProjectIdWithDetails(projectId);
        } else {
            resources = resourceRepository.findAllWithDetails();
            allEntries = dailyEffortRepository.findAll();
        }

        Map<Integer, BigDecimal> actualMdMap = new HashMap<>();
        for (DailyEffortEntry e : allEntries) {
            if (e.getResource() != null) {
                Integer rid = e.getResource().getResourceId();
                BigDecimal currentMd = actualMdMap.getOrDefault(rid, BigDecimal.ZERO);
                BigDecimal mdToAdd = e.getManDays() != null ? e.getManDays() : BigDecimal.ZERO;
                actualMdMap.put(rid, currentMd.add(mdToAdd));
            }
        }

        List<ResourceSummaryResponse> result = new ArrayList<>();
        BigDecimal totalPlanned = BigDecimal.ZERO;
        BigDecimal totalActual = BigDecimal.ZERO;
        BigDecimal totalLeft = BigDecimal.ZERO;
        BigDecimal totalCost = BigDecimal.ZERO;

        for (Resource r : resources) {
            String name = r.getUser() != null ? r.getUser().getFullName() : "Unknown";
            String role = r.getDeveloperName() != null ? r.getDeveloperName().getDeveloperName() : "Unknown";
            String level = r.getExperienceLevel() != null ? r.getExperienceLevel() : "-";
            String moduleName = r.getModule() != null ? r.getModule().getModuleName() : "-";

            BigDecimal rate = r.getRatePerDay() != null ? r.getRatePerDay() : BigDecimal.ZERO;
            BigDecimal planned = r.getPlannedManDays() != null ? r.getPlannedManDays() : BigDecimal.ZERO;
            BigDecimal actual = actualMdMap.getOrDefault(r.getResourceId(), BigDecimal.ZERO);

            BigDecimal left = planned.subtract(actual);
            BigDecimal cost = actual.multiply(rate);

            totalPlanned = totalPlanned.add(planned);
            totalActual = totalActual.add(actual);
            totalLeft = totalLeft.add(left);
            totalCost = totalCost.add(cost);

            String status = "On Track";
            if (actual.compareTo(planned) > 0) {
                status = "Overrun";
            } else if (actual.compareTo(planned.multiply(new BigDecimal("0.85"))) >= 0) {
                status = "Watch";
            }

            result.add(new ResourceSummaryResponse(
                    r.getResourceId(), name, role, level, moduleName, rate, planned, actual, left, cost, status
            ));
        }

        ResourceSummaryWrapperResponse.TotalMetrics totalMetrics = new ResourceSummaryWrapperResponse.TotalMetrics(
                totalPlanned, totalActual, totalLeft, totalCost
        );

        return new ResourceSummaryWrapperResponse(result, totalMetrics);
    }



    @Override
    public ModuleSummaryWrapperResponse getModuleSummary(Integer projectId) {
        List<Module> modules;
        List<DailyEffortEntry> allEntries;

        if (projectId != null) {
            modules = moduleRepository.findByProject_ProjectId(projectId);
            allEntries = dailyEffortRepository.findByProjectIdWithDetails(projectId);
        } else {
            modules = moduleRepository.findAll();
            allEntries = dailyEffortRepository.findAll();
        }

        Map<Integer, BigDecimal> actualMdMap = new HashMap<>();
        for (DailyEffortEntry e : allEntries) {
            if (e.getModule() != null) {
                Integer mid = e.getModule().getModuleId();
                BigDecimal currentMd = actualMdMap.getOrDefault(mid, BigDecimal.ZERO);
                BigDecimal mdToAdd = e.getManDays() != null ? e.getManDays() : BigDecimal.ZERO;
                actualMdMap.put(mid, currentMd.add(mdToAdd));
            }
        }

        List<ModuleSummaryResponse> result = new ArrayList<>();
        BigDecimal totalPlannedMd = BigDecimal.ZERO;
        BigDecimal totalPlannedCost = BigDecimal.ZERO;
        BigDecimal totalActualMd = BigDecimal.ZERO;
        for (Module m : modules) {
            BigDecimal plannedMd = m.getPlannedManDays() != null ? m.getPlannedManDays() : BigDecimal.ZERO;
            BigDecimal plannedCost = m.getPlannedCost() != null ? m.getPlannedCost() : BigDecimal.ZERO;
            BigDecimal actualMd = actualMdMap.getOrDefault(m.getModuleId(), BigDecimal.ZERO);

            totalPlannedMd = totalPlannedMd.add(plannedMd);
            totalPlannedCost = totalPlannedCost.add(plannedCost);
            totalActualMd = totalActualMd.add(actualMd);

            String percent = "0.0%";
            if (plannedMd.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal p = actualMd.divide(plannedMd, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
                percent = p.setScale(1, RoundingMode.HALF_UP).toString() + "%";
            }

            result.add(new ModuleSummaryResponse(
                    m.getModuleId(),
                    m.getModuleName(),
                    m.getScopeDescription(),
                    plannedMd,
                    plannedCost,
                    actualMd,
                    percent
            ));
        }

        String totalPercent = "0.0%";
        if (totalPlannedMd.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal p = totalActualMd.divide(totalPlannedMd, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
            totalPercent = p.setScale(1, RoundingMode.HALF_UP).toString() + "%";
        }

        ModuleSummaryWrapperResponse.ModuleTotalMetrics totalMetrics = new ModuleSummaryWrapperResponse.ModuleTotalMetrics(
                totalPlannedMd, totalPlannedCost, totalActualMd, totalPercent
        );

        return new ModuleSummaryWrapperResponse(result, totalMetrics);
    }

    @Override
    public EffortEntryWrapperResponse getAllEffortEntries(Integer projectId) {
        List<DailyEffortEntry> allEntries;
        if (projectId != null) {
            allEntries = dailyEffortRepository.findByProjectIdWithDetails(projectId);
        } else {
            allEntries = dailyEffortRepository.findAllWithDetails();
        }

        List<EffortEntryResponse> entries = allEntries.stream().map(e -> {
            String resourceName = (e.getResource() != null && e.getResource().getUser() != null)
                    ? e.getResource().getUser().getFullName() : "-";
            String weekName = e.getWeek() != null ? e.getWeek().getWeekName() : "-";
            String moduleName = e.getModule() != null ? e.getModule().getModuleName() : "-";
            String activityName = e.getActivity() != null ? e.getActivity().getActivityName() : "-";
            String status = e.getStatus() != null ? e.getStatus().getStatusName() : "-";

            return new EffortEntryResponse(
                    e.getEntryId(),
                    resourceName,
                    e.getWorkDate(),
                    weekName,
                    moduleName,
                    activityName,
                    e.getHoursWorked() != null ? e.getHoursWorked() : BigDecimal.ZERO,
                    e.getManDays() != null ? e.getManDays() : BigDecimal.ZERO,
                    status,
                    e.getWorkDescription()
            );
        }).collect(Collectors.toList());

        BigDecimal totalHours = entries.stream().map(EffortEntryResponse::getHours).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalMd = entries.stream().map(EffortEntryResponse::getManDays).reduce(BigDecimal.ZERO, BigDecimal::add);

        return new EffortEntryWrapperResponse(entries, new EffortEntryWrapperResponse.EffortTotalMetrics(totalHours, totalMd));
    }

    @Override
    public EffortEntryWrapperResponse getEffortEntriesByResource(Integer resourceId, Integer projectId) {
        List<DailyEffortEntry> entryList;
        if (projectId != null) {
            entryList = dailyEffortRepository.findByResourceIdAndProjectIdWithDetails(resourceId, projectId);
        } else {
            entryList = dailyEffortRepository.findByResourceIdWithDetails(resourceId);
        }

        List<EffortEntryResponse> entries = entryList.stream().map(e -> {
            String resourceName = (e.getResource() != null && e.getResource().getUser() != null)
                    ? e.getResource().getUser().getFullName() : "-";
            String weekName = e.getWeek() != null ? e.getWeek().getWeekName() : "-";
            String moduleName = e.getModule() != null ? e.getModule().getModuleName() : "-";
            String activityName = e.getActivity() != null ? e.getActivity().getActivityName() : "-";
            String status = e.getStatus() != null ? e.getStatus().getStatusName() : "-";

            return new EffortEntryResponse(
                    e.getEntryId(),
                    resourceName,
                    e.getWorkDate(),
                    weekName,
                    moduleName,
                    activityName,
                    e.getHoursWorked() != null ? e.getHoursWorked() : BigDecimal.ZERO,
                    e.getManDays() != null ? e.getManDays() : BigDecimal.ZERO,
                    status,
                    e.getWorkDescription()
            );
        }).collect(Collectors.toList());

        BigDecimal totalHours = entries.stream().map(EffortEntryResponse::getHours).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalMd = entries.stream().map(EffortEntryResponse::getManDays).reduce(BigDecimal.ZERO, BigDecimal::add);

        return new EffortEntryWrapperResponse(entries, new EffortEntryWrapperResponse.EffortTotalMetrics(totalHours, totalMd));
    }
}

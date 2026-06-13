package com.hospital.Arogeva.service;

import com.hospital.Arogeva.entity.*;
import com.hospital.Arogeva.entity.Module;
import com.hospital.Arogeva.payload.DailyEffortRequest;
import com.hospital.Arogeva.payload.DailyEffortResponse;
import com.hospital.Arogeva.payload.ResourceDropdownResponse;
import com.hospital.Arogeva.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import com.hospital.Arogeva.payload.ProjectWeekResponse;
import com.hospital.Arogeva.payload.EffortEntryResponse;
import com.hospital.Arogeva.payload.EffortEntryWrapperResponse;
import java.math.BigDecimal;

@Service
public class DailyEffortService {

    @Autowired
    private DailyEffortRepository dailyEffortRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectWeekRepository projectWeekRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private ActivityTypeRepository activityTypeRepository;

    @Autowired
    private WorkStatusRepository workStatusRepository;

    @Autowired
    private UserRepository userRepository;

    public DailyEffortResponse saveOrUpdateEffort(DailyEffortRequest request, String currentUserId) {
        try {
            DailyEffortEntry entry;
            
            if (request.getEntryId() != null) {

                entry = dailyEffortRepository.findById(request.getEntryId())
                        .orElseThrow(() -> new RuntimeException("Entry not found with ID: " + request.getEntryId()));
            } else {

                entry = new DailyEffortEntry();
                entry.setCreatedAt(LocalDateTime.now());
                
                if (currentUserId != null) {
                    entry.setCreatedBy(userRepository.findByUserId(currentUserId)
                            .orElseThrow(() -> new RuntimeException("User not found: " + currentUserId)));
                }
            }

            if (request.getResourceId() != null) {
                entry.setResource(resourceRepository.getReferenceById(request.getResourceId()));
            }
            if (request.getProjectId() != null) {
                entry.setProject(projectRepository.getReferenceById(request.getProjectId()));
            }
            if (request.getWeekId() != null) {
                entry.setWeek(projectWeekRepository.getReferenceById(request.getWeekId()));
            }
            if (request.getModuleId() != null) {
                entry.setModule(moduleRepository.getReferenceById(request.getModuleId()));
            }
            if (request.getActivityId() != null) {
                entry.setActivity(activityTypeRepository.getReferenceById(request.getActivityId()));
            }
            if (request.getStatusId() != null) {
                entry.setStatus(workStatusRepository.getReferenceById(request.getStatusId()));
            }

            
            entry.setWorkDate(request.getWorkDate());
            entry.setHoursWorked(request.getHoursWorked());
            entry.setManDays(request.getManDays());
            entry.setWorkDescription(request.getWorkDescription());

            DailyEffortEntry savedEntry = dailyEffortRepository.save(entry);

            String message = request.getEntryId() == null ? "Entry created successfully" : "Entry updated successfully";
            return new DailyEffortResponse(true, message, savedEntry.getEntryId());

        } catch (Exception e) {
            return new DailyEffortResponse(false, "Error saving entry: " + e.getMessage(), null);
        }
    }


    public List<ProjectWeekResponse> getAllProjectWeeks() {
        return projectWeekRepository.findAll().stream()
                .map(pw -> new ProjectWeekResponse(
                        pw.getWeekId(),
                        pw.getProject() != null ? pw.getProject().getProjectId() : null,
                        pw.getProject() != null ? pw.getProject().getProjectName() : "Unknown",
                        pw.getWeekName(),
                        pw.getStartDate(),
                        pw.getEndDate(),
                        pw.getCreatedAt(),
                        pw.getUpdatedAt(),
                        "Success",
                        true
                ))
                .collect(Collectors.toList());
    }

    public List<ResourceDropdownResponse> getAllResources() {
        return resourceRepository.findAllWithDetails().stream().map(r -> {
            String name = r.getUser() != null ? r.getUser().getFullName() : "Unknown";
            String role = r.getDeveloperName() != null ? r.getDeveloperName().getDeveloperName() : "Unknown";
            return new ResourceDropdownResponse(
                    r.getResourceId(),
                    name,
                    role
            );
        }).collect(Collectors.toList());
    }

    public EffortEntryWrapperResponse getMyEffortEntries(Integer weekId, String userId) {
        List<DailyEffortEntry> entryList = dailyEffortRepository.findByWeekIdAndUserIdForLogs(weekId, userId);

        List<EffortEntryResponse> entries = entryList.stream().map(e -> {
            String resourceName = e.getCreatedBy() != null ? e.getCreatedBy().getFullName() : "-";
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

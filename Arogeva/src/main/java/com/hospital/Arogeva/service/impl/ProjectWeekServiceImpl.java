package com.hospital.Arogeva.service.impl;

import com.hospital.Arogeva.entity.Project;
import com.hospital.Arogeva.entity.ProjectWeek;
import com.hospital.Arogeva.payload.ProjectWeekRequest;
import com.hospital.Arogeva.payload.ProjectWeekResponse;
import com.hospital.Arogeva.payload.ProjectWeekResponseDTO;
import com.hospital.Arogeva.repository.ProjectRepository;
import com.hospital.Arogeva.repository.ProjectWeekRepository;
import com.hospital.Arogeva.service.ProjectWeekService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectWeekServiceImpl implements ProjectWeekService {

    @Autowired
    private ProjectWeekRepository projectWeekRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public ProjectWeekResponse createOrUpdateProjectWeek(ProjectWeekRequest request) {
        ProjectWeekResponse response = new ProjectWeekResponse();
        
        try {

            Optional<Project> project = projectRepository.findById(request.getProjectId());
            if (!project.isPresent()) {
                response.setSuccess(false);
                response.setMessage("Project with ID " + request.getProjectId() + " not found");
                return response;
            }

            ProjectWeek projectWeek;
            

            if (request.getWeekId() != null) {
                Optional<ProjectWeek> existing = projectWeekRepository.findById(request.getWeekId());
                
                if (existing.isPresent()) {

                    projectWeek = existing.get();
                    projectWeek.setProject(project.get());
                    projectWeek.setWeekName(request.getWeekName());
                    projectWeek.setStartDate(request.getStartDate());
                    projectWeek.setEndDate(request.getEndDate());
                    projectWeek.setUpdatedAt(LocalDateTime.now());
                    projectWeekRepository.save(projectWeek);
                    response.setSuccess(true);
                    response.setMessage("Project Week updated successfully");
                } else {
                    response.setSuccess(false);
                    response.setMessage("Project Week with ID " + request.getWeekId() + " not found");
                    return response;
                }
            } else {

                projectWeek = new ProjectWeek();
                projectWeek.setProject(project.get());
                projectWeek.setWeekName(request.getWeekName());
                projectWeek.setStartDate(request.getStartDate());
                projectWeek.setEndDate(request.getEndDate());
                projectWeek.setCreatedAt(LocalDateTime.now());
                projectWeekRepository.save(projectWeek);
                response.setSuccess(true);
                response.setMessage("Project Week created successfully");
            }
            

            response.setWeekId(projectWeek.getWeekId());
            response.setProjectId(projectWeek.getProject().getProjectId());
            response.setProjectName(projectWeek.getProject().getProjectName());
            response.setWeekName(projectWeek.getWeekName());
            response.setStartDate(projectWeek.getStartDate());
            response.setEndDate(projectWeek.getEndDate());
            response.setCreatedAt(projectWeek.getCreatedAt());
            response.setUpdatedAt(projectWeek.getUpdatedAt());
            
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Error: " + e.getMessage());
        }
        
        return response;
    }

    @Override
    public List<ProjectWeekResponse> getAllProjectWeeks() {
        return projectWeekRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProjectWeekResponse getProjectWeekById(Integer weekId) {
        Optional<ProjectWeek> projectWeek = projectWeekRepository.findById(weekId);
        
        if (projectWeek.isPresent()) {
            ProjectWeekResponse response = convertToResponse(projectWeek.get());
            response.setSuccess(true);
            return response;
        }
        
        ProjectWeekResponse response = new ProjectWeekResponse();
        response.setSuccess(false);
        response.setMessage("Project Week with ID " + weekId + " not found");
        return response;
    }

    @Override
    public List<ProjectWeekResponse> getWeeksByProjectId(Integer projectId) {
        List<ProjectWeek> projectWeeks = projectWeekRepository.findAll()
                .stream()
                .filter(pw -> pw.getProject().getProjectId().equals(projectId))
                .collect(Collectors.toList());
        
        return projectWeeks.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProjectWeekResponse deleteProjectWeek(Integer weekId) {
        ProjectWeekResponse response = new ProjectWeekResponse();
        
        try {
            Optional<ProjectWeek> projectWeek = projectWeekRepository.findById(weekId);
            
            if (projectWeek.isPresent()) {
                projectWeekRepository.deleteById(weekId);
                response.setSuccess(true);
                response.setMessage("Project Week deleted successfully");
            } else {
                response.setSuccess(false);
                response.setMessage("Project Week with ID " + weekId + " not found");
            }
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Error: " + e.getMessage());
        }
        
        return response;
    }


    @Override
    public List<ProjectWeekResponseDTO> getAllWeeks() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return projectWeekRepository.findAll()
                .stream()
                .map(week -> new ProjectWeekResponseDTO(
                        week.getWeekId(),
                        week.getWeekName() + " (" +
                                week.getStartDate().format(formatter) +
                                " to " +
                                week.getEndDate().format(formatter) +
                                ")"
                ))
                .toList();
    }


    private ProjectWeekResponse convertToResponse(ProjectWeek projectWeek) {
        ProjectWeekResponse response = new ProjectWeekResponse();
        response.setWeekId(projectWeek.getWeekId());
        response.setProjectId(projectWeek.getProject().getProjectId());
        response.setProjectName(projectWeek.getProject().getProjectName());
        response.setWeekName(projectWeek.getWeekName());
        response.setStartDate(projectWeek.getStartDate());
        response.setEndDate(projectWeek.getEndDate());
        response.setCreatedAt(projectWeek.getCreatedAt());
        response.setUpdatedAt(projectWeek.getUpdatedAt());
        response.setSuccess(true);
        return response;
    }
}

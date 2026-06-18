package com.hospital.Arogeva.service.impl;

import com.hospital.Arogeva.entity.Project;
import com.hospital.Arogeva.payload.ProjectRequest;
import com.hospital.Arogeva.payload.ProjectResponse;
import com.hospital.Arogeva.repository.ProjectRepository;
import com.hospital.Arogeva.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public ProjectResponse createOrUpdateProject(ProjectRequest request) {
        ProjectResponse response = new ProjectResponse();
        
        try {
            Project project;
            

            if (request.getProjectId() != null) {
                Optional<Project> existing = projectRepository.findById(request.getProjectId());
                
                if (existing.isPresent()) {

                    project = existing.get();
                    project.setProjectName(request.getProjectName());
                    project.setDescription(request.getDescription());
                    project.setPlannedManDays(request.getPlannedManDays());
                    project.setPlannedBudget(request.getPlannedBudget());
                    project.setStartDate(request.getStartDate());
                    project.setEndDate(request.getEndDate());
                    project.setArchitecture(request.getArchitecture());
                    project.setUpdatedAt(LocalDateTime.now());
                    project.setStatus(request.getStatus());
                    project.setProjectManager(request.getProjectManager());
                    project.setClient(request.getClient());
                    projectRepository.save(project);
                    response.setSuccess(true);
                    response.setMessage("Project updated successfully");
                } else {
                    response.setSuccess(false);
                    response.setMessage("Project with ID " + request.getProjectId() + " not found");
                    return response;
                }
            } else {

                project = new Project();
                project.setProjectName(request.getProjectName());
                project.setDescription(request.getDescription());
                project.setPlannedManDays(request.getPlannedManDays());
                project.setPlannedBudget(request.getPlannedBudget());
                project.setStartDate(request.getStartDate());
                project.setEndDate(request.getEndDate());
                project.setArchitecture(request.getArchitecture());
                project.setCreatedAt(LocalDateTime.now());
                project.setStatus(request.getStatus());
                project.setProjectManager(request.getProjectManager());
                project.setClient(request.getClient());
                projectRepository.save(project);
                response.setSuccess(true);
                response.setMessage("Project created successfully");
            }

            response.setProjectId(project.getProjectId());
            response.setProjectName(project.getProjectName());
            response.setDescription(project.getDescription());
            response.setPlannedManDays(project.getPlannedManDays());
            response.setPlannedBudget(project.getPlannedBudget());
            response.setStartDate(project.getStartDate());
            response.setEndDate(project.getEndDate());
            response.setArchitecture(project.getArchitecture());
            response.setCreatedAt(project.getCreatedAt());
            response.setUpdatedAt(project.getUpdatedAt());
            response.setStatus(project.getStatus());
            response.setProjectManager(project.getProjectManager());
            response.setClient(project.getClient());
            
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Error: " + e.getMessage());
        }
        
        return response;
    }

    @Override
    public List<ProjectResponse> getAllProjects() {
        return projectRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectResponse> getProjectsByStatus(String status) {
        return projectRepository.findByStatus(status)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProjectResponse getProjectById(Integer projectId) {
        Optional<Project> project = projectRepository.findById(projectId);
        
        if (project.isPresent()) {
            ProjectResponse response = convertToResponse(project.get());
            response.setSuccess(true);
            return response;
        }
        
        ProjectResponse response = new ProjectResponse();
        response.setSuccess(false);
        response.setMessage("Project with ID " + projectId + " not found");
        return response;
    }



    @Override
    public ProjectResponse deleteProject(Integer projectId) {
        ProjectResponse response = new ProjectResponse();
        
        try {
            Optional<Project> project = projectRepository.findById(projectId);
            
            if (project.isPresent()) {
                projectRepository.deleteById(projectId);
                response.setSuccess(true);
                response.setMessage("Project deleted successfully");
            } else {
                response.setSuccess(false);
                response.setMessage("Project with ID " + projectId + " not found");
            }
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Error: " + e.getMessage());
        }
        
        return response;
    }

    private ProjectResponse convertToResponse(Project project) {
        ProjectResponse response = new ProjectResponse();
        response.setProjectId(project.getProjectId());
        response.setProjectName(project.getProjectName());
        response.setDescription(project.getDescription());
        response.setPlannedManDays(project.getPlannedManDays());
        response.setPlannedBudget(project.getPlannedBudget());
        response.setStartDate(project.getStartDate());
        response.setEndDate(project.getEndDate());
        response.setArchitecture(project.getArchitecture());
        response.setCreatedAt(project.getCreatedAt());
        response.setUpdatedAt(project.getUpdatedAt());
        response.setStatus(project.getStatus());
        response.setProjectManager(project.getProjectManager());
        response.setClient(project.getClient());
        response.setSuccess(true);
        return response;
    }
}

package com.hospital.Arogeva.service;

import com.hospital.Arogeva.payload.ProjectRequest;
import com.hospital.Arogeva.payload.ProjectResponse;
import java.util.List;

public interface ProjectService {
    

    ProjectResponse createOrUpdateProject(ProjectRequest request);
    

    List<ProjectResponse> getAllProjects();
    
    List<ProjectResponse> getProjectsByStatus(String status);

    ProjectResponse getProjectById(Integer projectId);

    ProjectResponse deleteProject(Integer projectId);

    List<String> getAllArchitectures();
    
    List<String> getAllProjectManagers();
    
//    List<String> getAllProjectNames();
}

package com.hospital.Arogeva.service;

import com.hospital.Arogeva.payload.ProjectWeekRequest;
import com.hospital.Arogeva.payload.ProjectWeekResponse;
import com.hospital.Arogeva.payload.ProjectWeekResponseDTO;

import java.util.List;

public interface ProjectWeekService {

    ProjectWeekResponse createOrUpdateProjectWeek(ProjectWeekRequest request);

    List<ProjectWeekResponse> getAllProjectWeeks();

    ProjectWeekResponse getProjectWeekById(Integer weekId);

    List<ProjectWeekResponse> getWeeksByProjectId(Integer projectId);

    ProjectWeekResponse deleteProjectWeek(Integer weekId);


    List<ProjectWeekResponseDTO> getAllWeeks();
}

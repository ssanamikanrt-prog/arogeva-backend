package com.hospital.Arogeva.service.impl;

import com.hospital.Arogeva.entity.Resource;
import com.hospital.Arogeva.entity.User;
import com.hospital.Arogeva.entity.DeveloperMaster;
import com.hospital.Arogeva.entity.Project;
import com.hospital.Arogeva.entity.Module;
import com.hospital.Arogeva.payload.ResourceRequest;
import com.hospital.Arogeva.payload.ResourceResponse;
import com.hospital.Arogeva.repository.ResourceRepository;
import com.hospital.Arogeva.repository.UserRepository;
import com.hospital.Arogeva.repository.DeveloperMasterRepository;
import com.hospital.Arogeva.repository.ProjectRepository;
import com.hospital.Arogeva.repository.ModuleRepository;
import com.hospital.Arogeva.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeveloperMasterRepository developerMasterRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Override
    public ResourceResponse createOrUpdateResource(ResourceRequest request) {
        ResourceResponse response = new ResourceResponse();
        
        try {
            Optional<User> user = userRepository.findByUserId(request.getUserId());
            if (!user.isPresent()) {
                response.setSuccess(false);
                response.setMessage("User with ID " + request.getUserId() + " not found");
                return response;
            }

            Optional<DeveloperMaster> developerMaster = developerMasterRepository.findById(request.getDeveloperTypeId());
            if (!developerMaster.isPresent()) {
                response.setSuccess(false);
                response.setMessage("Developer Type with ID " + request.getDeveloperTypeId() + " not found");
                return response;
            }

            Optional<Project> project = projectRepository.findById(request.getProjectId());
            if (!project.isPresent()) {
                response.setSuccess(false);
                response.setMessage("Project with ID " + request.getProjectId() + " not found");
                return response;
            }

            Optional<Module> module = null;
            if (request.getModuleId() != null) {
                module = moduleRepository.findById(request.getModuleId());
                if (module != null && !module.isPresent()) {
                    response.setSuccess(false);
                    response.setMessage("Module with ID " + request.getModuleId() + " not found");
                    return response;
                }
            }

            Resource resource;
            

            if (request.getResourceId() != null) {
                Optional<Resource> existing = resourceRepository.findById(request.getResourceId());
                
                if (existing.isPresent()) {

                    resource = existing.get();
                    resource.setUser(user.get());
                    resource.setDeveloperName(developerMaster.get());
                    resource.setProject(project.get());
                    if (module != null && module.isPresent()) {
                        resource.setModule(module.get());
                    }
                    resource.setExperienceLevel(request.getExperienceLevel());
                    resource.setRatePerDay(request.getRatePerDay());
                    resource.setPlannedManDays(request.getPlannedManDays());
                    resource.setRemarks(request.getRemarks());
                    resource.setUpdatedAt(LocalDateTime.now());
                    resourceRepository.save(resource);
                    response.setSuccess(true);
                    response.setMessage("Resource updated successfully");
                } else {
                    response.setSuccess(false);
                    response.setMessage("Resource with ID " + request.getResourceId() + " not found");
                    return response;
                }
            } else {
                resource = new Resource();
                resource.setUser(user.get());
                resource.setDeveloperName(developerMaster.get());
                resource.setProject(project.get());
                if (module != null && module.isPresent()) {
                    resource.setModule(module.get());
                }
                resource.setExperienceLevel(request.getExperienceLevel());
                resource.setRatePerDay(request.getRatePerDay());
                resource.setPlannedManDays(request.getPlannedManDays());
                resource.setRemarks(request.getRemarks());
                resource.setCreatedAt(LocalDateTime.now());
                resourceRepository.save(resource);
                response.setSuccess(true);
                response.setMessage("Resource created successfully");
            }
            

            response.setResourceId(resource.getResourceId());
            response.setUserId(resource.getUser().getUserId());
            response.setUserName(resource.getUser().getFullName());
            response.setDeveloperTypeId(resource.getDeveloperName().getDeveloperTypeId());
            response.setDeveloperName(resource.getDeveloperName().getDeveloperName());
            response.setProjectId(resource.getProject().getProjectId());
            response.setProjectName(resource.getProject().getProjectName());
            if (resource.getModule() != null) {
                response.setModuleId(resource.getModule().getModuleId());
                response.setModuleName(resource.getModule().getModuleName());
            }
            response.setExperienceLevel(resource.getExperienceLevel());
            response.setRatePerDay(resource.getRatePerDay());
            response.setPlannedManDays(resource.getPlannedManDays());
            response.setRemarks(resource.getRemarks());
            response.setCreatedAt(resource.getCreatedAt());
            response.setUpdatedAt(resource.getUpdatedAt());
            
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Error: " + e.getMessage());
        }
        
        return response;
    }

    @Override
    public List<ResourceResponse> getAllResources() {
        return resourceRepository.findAllWithDetails()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ResourceResponse getResourceById(Integer resourceId) {
        Optional<Resource> resource = resourceRepository.findById(resourceId);
        
        if (resource.isPresent()) {
            ResourceResponse response = convertToResponse(resource.get());
            response.setSuccess(true);
            return response;
        }
        
        ResourceResponse response = new ResourceResponse();
        response.setSuccess(false);
        response.setMessage("Resource with ID " + resourceId + " not found");
        return response;
    }

    @Override
    public List<ResourceResponse> getResourcesByProjectId(Integer projectId) {
        List<Resource> resources = resourceRepository.findAllWithDetails()
                .stream()
                .filter(r -> r.getProject().getProjectId().equals(projectId))
                .collect(Collectors.toList());
        
        return resources.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ResourceResponse> getResourcesByModuleId(Integer moduleId) {
        List<Resource> resources = resourceRepository.findAllWithDetails()
                .stream()
                .filter(r -> r.getModule() != null && r.getModule().getModuleId().equals(moduleId))
                .collect(Collectors.toList());
        
        return resources.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ResourceResponse> getResourcesByUserId(String userId) {
        List<Resource> resources = resourceRepository.findAllWithDetails()
                .stream()
                .filter(r -> r.getUser().getUserId().equals(userId))
                .collect(Collectors.toList());
        
        return resources.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ResourceResponse deleteResource(Integer resourceId) {
        ResourceResponse response = new ResourceResponse();
        
        try {
            Optional<Resource> resource = resourceRepository.findById(resourceId);
            
            if (resource.isPresent()) {
                resourceRepository.deleteById(resourceId);
                response.setSuccess(true);
                response.setMessage("Resource deleted successfully");
            } else {
                response.setSuccess(false);
                response.setMessage("Resource with ID " + resourceId + " not found");
            }
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Error: " + e.getMessage());
        }
        
        return response;
    }


    private ResourceResponse convertToResponse(Resource resource) {
        ResourceResponse response = new ResourceResponse();
        response.setResourceId(resource.getResourceId());
        response.setUserId(resource.getUser() != null ? resource.getUser().getUserId() : null);
        response.setUserName(resource.getUser() != null ? resource.getUser().getFullName() : "Unknown");
        response.setDeveloperTypeId(resource.getDeveloperName() != null ? resource.getDeveloperName().getDeveloperTypeId() : null);
        response.setDeveloperName(resource.getDeveloperName() != null ? resource.getDeveloperName().getDeveloperName() : "Unknown");
        response.setProjectId(resource.getProject() != null ? resource.getProject().getProjectId() : null);
        response.setProjectName(resource.getProject() != null ? resource.getProject().getProjectName() : "Unknown");
        if (resource.getModule() != null) {
            response.setModuleId(resource.getModule().getModuleId());
            response.setModuleName(resource.getModule().getModuleName());
        }
        response.setExperienceLevel(resource.getExperienceLevel());
        response.setRatePerDay(resource.getRatePerDay());
        response.setPlannedManDays(resource.getPlannedManDays());
        response.setRemarks(resource.getRemarks());
        response.setCreatedAt(resource.getCreatedAt());
        response.setUpdatedAt(resource.getUpdatedAt());
        response.setSuccess(true);
        return response;
    }
}

package com.hospital.Arogeva.service.impl;

import com.hospital.Arogeva.entity.Module;
import com.hospital.Arogeva.entity.Project;
import com.hospital.Arogeva.payload.ModuleRequest;
import com.hospital.Arogeva.payload.ModuleResponse;
import com.hospital.Arogeva.repository.ModuleRepository;
import com.hospital.Arogeva.repository.ProjectRepository;
import com.hospital.Arogeva.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ModuleServiceImpl implements ModuleService {

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public ModuleResponse createOrUpdateModule(ModuleRequest request) {


        ModuleResponse response = new ModuleResponse();
        
        try {
            Optional<Project> project = projectRepository.findById(request.getProjectId());
            if (!project.isPresent()) {
                response.setSuccess(false);
                response.setMessage("Project with ID " + request.getProjectId() + " not found");
                return response;
            }

            Module module;
            

            if (request.getModuleId() != null) {
                Optional<Module> existing = moduleRepository.findById(request.getModuleId());
                
                if (existing.isPresent()) {

                    module = existing.get();
                    module.setProject(project.get());
                    module.setModuleName(request.getModuleName());
                    module.setScopeDescription(request.getScopeDescription());
                    module.setPlannedManDays(request.getPlannedManDays());
                    module.setPlannedCost(request.getPlannedCost());
                    module.setUpdatedAt(LocalDateTime.now());
                    moduleRepository.save(module);
                    response.setSuccess(true);
                    response.setMessage("Module updated successfully");
                } else {
                    response.setSuccess(false);
                    response.setMessage("Module with ID " + request.getModuleId() + " not found");
                    return response;
                }
            } else {
                module = new Module();
                module.setProject(project.get());
                module.setModuleName(request.getModuleName());
                module.setScopeDescription(request.getScopeDescription());
                module.setPlannedManDays(request.getPlannedManDays());
                module.setPlannedCost(request.getPlannedCost());
                module.setCreatedAt(LocalDateTime.now());
                moduleRepository.save(module);
                response.setSuccess(true);
                response.setMessage("Module created successfully");
            }
            

            response.setModuleId(module.getModuleId());
            response.setProjectId(module.getProject().getProjectId());
            response.setProjectName(module.getProject().getProjectName());
            response.setModuleName(module.getModuleName());
            response.setScopeDescription(module.getScopeDescription());
            response.setPlannedManDays(module.getPlannedManDays());
            response.setPlannedCost(module.getPlannedCost());
            response.setCreatedAt(module.getCreatedAt());
            response.setUpdatedAt(module.getUpdatedAt());
            
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Error: " + e.getMessage());
        }
        
        return response;
    }

    @Override
    public List<ModuleResponse> getAllModules() {
        return moduleRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ModuleResponse getModuleById(Integer moduleId) {
        Optional<Module> module = moduleRepository.findById(moduleId);
        
        if (module.isPresent()) {
            ModuleResponse response = convertToResponse(module.get());
            response.setSuccess(true);
            return response;
        }
        
        ModuleResponse response = new ModuleResponse();
        response.setSuccess(false);
        response.setMessage("Module with ID " + moduleId + " not found");
        return response;
    }

    @Override
    public List<ModuleResponse> getModulesByProjectId(Integer projectId) {
        List<Module> modules = moduleRepository.findAll()
                .stream()
                .filter(m -> m.getProject().getProjectId().equals(projectId))
                .collect(Collectors.toList());
        
        return modules.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ModuleResponse deleteModule(Integer moduleId) {
        ModuleResponse response = new ModuleResponse();
        
        try {
            Optional<Module> module = moduleRepository.findById(moduleId);
            
            if (module.isPresent()) {
                moduleRepository.deleteById(moduleId);
                response.setSuccess(true);
                response.setMessage("Module deleted successfully");
            } else {
                response.setSuccess(false);
                response.setMessage("Module with ID " + moduleId + " not found");
            }
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Error: " + e.getMessage());
        }
        
        return response;
    }


    private ModuleResponse convertToResponse(Module module) {
        ModuleResponse response = new ModuleResponse();
        response.setModuleId(module.getModuleId());
        response.setProjectId(module.getProject().getProjectId());
        response.setProjectName(module.getProject().getProjectName());
        response.setModuleName(module.getModuleName());
        response.setScopeDescription(module.getScopeDescription());
        response.setPlannedManDays(module.getPlannedManDays());
        response.setPlannedCost(module.getPlannedCost());
        response.setCreatedAt(module.getCreatedAt());
        response.setUpdatedAt(module.getUpdatedAt());
        response.setSuccess(true);
        return response;
    }
}

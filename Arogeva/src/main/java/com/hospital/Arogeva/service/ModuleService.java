package com.hospital.Arogeva.service;

import com.hospital.Arogeva.payload.ModuleRequest;
import com.hospital.Arogeva.payload.ModuleResponse;
import java.util.List;

public interface ModuleService {
    

    ModuleResponse createOrUpdateModule(ModuleRequest request);
    

    List<ModuleResponse> getAllModules();
    

    ModuleResponse getModuleById(Integer moduleId);
    

    List<ModuleResponse> getModulesByProjectId(Integer projectId);
    

    ModuleResponse deleteModule(Integer moduleId);
}

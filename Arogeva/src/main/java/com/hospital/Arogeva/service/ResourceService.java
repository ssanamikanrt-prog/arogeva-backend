package com.hospital.Arogeva.service;

import com.hospital.Arogeva.payload.ResourceRequest;
import com.hospital.Arogeva.payload.ResourceResponse;
import java.util.List;

public interface ResourceService {
    

    ResourceResponse createOrUpdateResource(ResourceRequest request);
    

    List<ResourceResponse> getAllResources();
    

    ResourceResponse getResourceById(Integer resourceId);
    

    List<ResourceResponse> getResourcesByProjectId(Integer projectId);
    

    List<ResourceResponse> getResourcesByModuleId(Integer moduleId);
    

    List<ResourceResponse> getResourcesByUserId(String userId);

    ResourceResponse deleteResource(Integer resourceId);
}

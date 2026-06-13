package com.hospital.Arogeva.service;

import com.hospital.Arogeva.payload.DeveloperMasterRequest;
import com.hospital.Arogeva.payload.DeveloperMasterResponse;
import java.util.List;

public interface DeveloperMasterService {

    DeveloperMasterResponse createOrUpdateDeveloperMaster(DeveloperMasterRequest request);
    

    List<DeveloperMasterResponse> getAllDeveloperMasters();
    

    DeveloperMasterResponse getDeveloperMasterById(Integer developerTypeId);
    

    DeveloperMasterResponse deleteDeveloperMaster(Integer developerTypeId);
}

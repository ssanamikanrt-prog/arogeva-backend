package com.hospital.Arogeva.service.impl;

import com.hospital.Arogeva.entity.DeveloperMaster;
import com.hospital.Arogeva.payload.DeveloperMasterRequest;
import com.hospital.Arogeva.payload.DeveloperMasterResponse;
import com.hospital.Arogeva.repository.DeveloperMasterRepository;
import com.hospital.Arogeva.service.DeveloperMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DeveloperMasterServiceImpl implements DeveloperMasterService {

    @Autowired
    private DeveloperMasterRepository developerMasterRepository;

    @Override
    public DeveloperMasterResponse createOrUpdateDeveloperMaster(DeveloperMasterRequest request) {
        DeveloperMasterResponse response = new DeveloperMasterResponse();
        
        try {
            DeveloperMaster developerMaster;

            if (request.getDeveloperTypeId() != null) {
                Optional<DeveloperMaster> existing = developerMasterRepository.findById(request.getDeveloperTypeId());
                
                if (existing.isPresent()) {
                    developerMaster = existing.get();
                    developerMaster.setDeveloperName(request.getDeveloperName());
                    developerMaster.setDescription(request.getDescription());
                    developerMaster.setUpdatedAt(LocalDateTime.now());
                    developerMasterRepository.save(developerMaster);
                    response.setSuccess(true);
                    response.setMessage("Developer Master updated successfully");
                } else {
                    response.setSuccess(false);
                    response.setMessage("Developer Master with ID " + request.getDeveloperTypeId() + " not found");
                    return response;
                }
            } else {

                developerMaster = new DeveloperMaster();
                developerMaster.setDeveloperName(request.getDeveloperName());
                developerMaster.setDescription(request.getDescription());
                developerMaster.setCreatedAt(LocalDateTime.now());
                developerMasterRepository.save(developerMaster);
                response.setSuccess(true);
                response.setMessage("Developer Master created successfully");
            }
            

            response.setDeveloperTypeId(developerMaster.getDeveloperTypeId());
            response.setDeveloperName(developerMaster.getDeveloperName());
            response.setDescription(developerMaster.getDescription());
            response.setCreatedAt(developerMaster.getCreatedAt());
            response.setUpdatedAt(developerMaster.getUpdatedAt());
            
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Error: " + e.getMessage());
        }
        
        return response;
    }

    @Override
    public List<DeveloperMasterResponse> getAllDeveloperMasters() {
        return developerMasterRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DeveloperMasterResponse getDeveloperMasterById(Integer developerTypeId) {
        Optional<DeveloperMaster> developerMaster = developerMasterRepository.findById(developerTypeId);
        
        if (developerMaster.isPresent()) {
            DeveloperMasterResponse response = convertToResponse(developerMaster.get());
            response.setSuccess(true);
            return response;
        }
        
        DeveloperMasterResponse response = new DeveloperMasterResponse();
        response.setSuccess(false);
        response.setMessage("Developer Master with ID " + developerTypeId + " not found");
        return response;
    }

    @Override
    public DeveloperMasterResponse deleteDeveloperMaster(Integer developerTypeId) {
        DeveloperMasterResponse response = new DeveloperMasterResponse();
        
        try {
            Optional<DeveloperMaster> developerMaster = developerMasterRepository.findById(developerTypeId);
            
            if (developerMaster.isPresent()) {
                developerMasterRepository.deleteById(developerTypeId);
                response.setSuccess(true);
                response.setMessage("Developer Master deleted successfully");
            } else {
                response.setSuccess(false);
                response.setMessage("Developer Master with ID " + developerTypeId + " not found");
            }
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Error: " + e.getMessage());
        }
        
        return response;
    }


    private DeveloperMasterResponse convertToResponse(DeveloperMaster developerMaster) {
        DeveloperMasterResponse response = new DeveloperMasterResponse();
        response.setDeveloperTypeId(developerMaster.getDeveloperTypeId());
        response.setDeveloperName(developerMaster.getDeveloperName());
        response.setDescription(developerMaster.getDescription());
        response.setCreatedAt(developerMaster.getCreatedAt());
        response.setUpdatedAt(developerMaster.getUpdatedAt());
        response.setSuccess(true);
        return response;
    }
}

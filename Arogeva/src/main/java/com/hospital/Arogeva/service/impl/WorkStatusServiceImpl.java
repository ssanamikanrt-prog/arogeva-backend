package com.hospital.Arogeva.service.impl;

import com.hospital.Arogeva.entity.WorkStatus;
import com.hospital.Arogeva.payload.WorkStatusRequest;
import com.hospital.Arogeva.payload.WorkStatusResponse;
import com.hospital.Arogeva.repository.WorkStatusRepository;
import com.hospital.Arogeva.service.WorkStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WorkStatusServiceImpl implements WorkStatusService {

    @Autowired
    private WorkStatusRepository workStatusRepository;

    @Override
    public WorkStatusResponse createOrUpdateWorkStatus(WorkStatusRequest request) {
        WorkStatusResponse response = new WorkStatusResponse();
        
        try {
            WorkStatus workStatus;

            if (request.getStatusId() != null) {
                Optional<WorkStatus> existing = workStatusRepository.findById(request.getStatusId());
                
                if (existing.isPresent()) {

                    workStatus = existing.get();
                    workStatus.setStatusName(request.getStatusName());
                    workStatus.setUpdatedAt(LocalDateTime.now());
                    workStatusRepository.save(workStatus);
                    response.setSuccess(true);
                    response.setMessage("Work Status updated successfully");
                } else {
                    response.setSuccess(false);
                    response.setMessage("Work Status with ID " + request.getStatusId() + " not found");
                    return response;
                }
            } else {

                workStatus = new WorkStatus();
                workStatus.setStatusName(request.getStatusName());
                workStatus.setCreatedAt(LocalDateTime.now());
                workStatusRepository.save(workStatus);
                response.setSuccess(true);
                response.setMessage("Work Status created successfully");
            }

            response.setStatusId(workStatus.getStatusId());
            response.setStatusName(workStatus.getStatusName());
            response.setCreatedAt(workStatus.getCreatedAt());
            response.setUpdatedAt(workStatus.getUpdatedAt());
            
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Error: " + e.getMessage());
        }
        
        return response;
    }

    @Override
    public List<WorkStatusResponse> getAllWorkStatuses() {
        return workStatusRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public WorkStatusResponse getWorkStatusById(Integer statusId) {
        Optional<WorkStatus> workStatus = workStatusRepository.findById(statusId);
        
        if (workStatus.isPresent()) {
            WorkStatusResponse response = convertToResponse(workStatus.get());
            response.setSuccess(true);
            return response;
        }
        
        WorkStatusResponse response = new WorkStatusResponse();
        response.setSuccess(false);
        response.setMessage("Work Status with ID " + statusId + " not found");
        return response;
    }

    @Override
    public WorkStatusResponse deleteWorkStatus(Integer statusId) {
        WorkStatusResponse response = new WorkStatusResponse();
        
        try {
            Optional<WorkStatus> workStatus = workStatusRepository.findById(statusId);
            
            if (workStatus.isPresent()) {
                workStatusRepository.deleteById(statusId);
                response.setSuccess(true);
                response.setMessage("Work Status deleted successfully");
            } else {
                response.setSuccess(false);
                response.setMessage("Work Status with ID " + statusId + " not found");
            }
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Error: " + e.getMessage());
        }
        
        return response;
    }

    private WorkStatusResponse convertToResponse(WorkStatus workStatus) {
        WorkStatusResponse response = new WorkStatusResponse();
        response.setStatusId(workStatus.getStatusId());
        response.setStatusName(workStatus.getStatusName());
        response.setCreatedAt(workStatus.getCreatedAt());
        response.setUpdatedAt(workStatus.getUpdatedAt());
        response.setSuccess(true);
        return response;
    }
}

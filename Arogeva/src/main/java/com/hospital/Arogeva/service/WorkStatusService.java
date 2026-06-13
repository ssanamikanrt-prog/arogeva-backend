package com.hospital.Arogeva.service;

import com.hospital.Arogeva.payload.WorkStatusRequest;
import com.hospital.Arogeva.payload.WorkStatusResponse;
import java.util.List;

public interface WorkStatusService {
    

    WorkStatusResponse createOrUpdateWorkStatus(WorkStatusRequest request);
    

    List<WorkStatusResponse> getAllWorkStatuses();
    

    WorkStatusResponse getWorkStatusById(Integer statusId);
    

    WorkStatusResponse deleteWorkStatus(Integer statusId);
}

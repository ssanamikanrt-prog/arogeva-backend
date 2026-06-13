package com.hospital.Arogeva.payload;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ResourceRequest {

    private Integer resourceId;
    private String userId;
    private Integer developerTypeId;
    private Integer projectId;
    private Integer moduleId;
    private String experienceLevel;
    private BigDecimal ratePerDay;
    private BigDecimal plannedManDays;
    private String remarks;


}

package com.hospital.Arogeva.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceResponse {

    private Integer resourceId;
    private String userId;
    private String userName;
    private Integer developerTypeId;
    private String developerName;
    private Integer projectId;
    private String projectName;
    private Integer moduleId;
    private String moduleName;
    private String experienceLevel;
    private BigDecimal ratePerDay;
    private BigDecimal plannedManDays;
    private String remarks;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String message;
    private boolean success;

}

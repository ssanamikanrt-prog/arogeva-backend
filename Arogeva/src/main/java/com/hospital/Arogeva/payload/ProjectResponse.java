package com.hospital.Arogeva.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse {

    private Integer projectId;
    private String projectName;
    private String description;
    private BigDecimal plannedManDays;
    private BigDecimal plannedBudget;
    private LocalDate startDate;
    private LocalDate endDate;
    private String architecture;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String message;
    private boolean success;

    private String status;

    private String projectManagerId;
    private String projectManager;
    private String client;


}

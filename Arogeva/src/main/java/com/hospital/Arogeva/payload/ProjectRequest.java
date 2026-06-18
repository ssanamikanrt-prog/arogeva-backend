package com.hospital.Arogeva.payload;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProjectRequest {

    private Integer projectId;
    private String projectName;
    private String description;
    private BigDecimal plannedManDays;
    private BigDecimal plannedBudget;
    private LocalDate startDate;
    private LocalDate endDate;
    private String architecture;


    private String status;
    private String projectManager;
    private String client;

}

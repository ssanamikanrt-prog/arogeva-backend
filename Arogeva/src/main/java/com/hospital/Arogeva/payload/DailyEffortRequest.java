package com.hospital.Arogeva.payload;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DailyEffortRequest {

    private Integer entryId;
    private Integer resourceId;
    private Integer projectId;
    private Integer weekId;
    private Integer moduleId;
    private Integer activityId;
    private Integer statusId;
    
    private LocalDate workDate;
    private BigDecimal hoursWorked;
    private BigDecimal manDays;
    private String workDescription;


}

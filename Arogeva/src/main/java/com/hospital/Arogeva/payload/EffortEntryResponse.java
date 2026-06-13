package com.hospital.Arogeva.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EffortEntryResponse {

    private Integer entryId;
    private String resourceName;
    private LocalDate date;
    private String weekName;
    private String moduleName;
    private String activityName;
    private BigDecimal hours;
    private BigDecimal manDays;
    private String status;
    private String description;


}

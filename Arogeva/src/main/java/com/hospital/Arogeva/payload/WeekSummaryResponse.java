package com.hospital.Arogeva.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeekSummaryResponse {

    private Integer weekId;
    private String weekName;
    private String dateRange;
    private BigDecimal hours;
    private BigDecimal manDays;
    private BigDecimal cost;
    private Integer entries;

}

package com.hospital.Arogeva.payload;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ProjectWeekRequest {


    private Integer weekId;
    private Integer projectId;
    private String weekName;
    private LocalDate startDate;
    private LocalDate endDate;


}

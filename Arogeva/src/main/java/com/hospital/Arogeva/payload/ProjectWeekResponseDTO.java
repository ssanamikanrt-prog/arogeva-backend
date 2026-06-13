package com.hospital.Arogeva.payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectWeekResponseDTO {

    private Integer weekId;
    private String weekName;
}
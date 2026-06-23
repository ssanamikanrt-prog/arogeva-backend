package com.hospital.Arogeva.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManagerProjectResponse {
    private Integer projectId;
    private String projectName;
    private String projectManagerId;
    private String projectManager;
}

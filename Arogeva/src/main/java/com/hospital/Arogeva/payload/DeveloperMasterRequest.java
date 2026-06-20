package com.hospital.Arogeva.payload;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class DeveloperMasterRequest {

    private Integer developerTypeId;

    @NotBlank(message = "Developer name is required")
    private String developerName;

    private String description;


}

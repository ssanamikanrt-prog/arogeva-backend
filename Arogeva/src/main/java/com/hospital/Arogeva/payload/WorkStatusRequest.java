package com.hospital.Arogeva.payload;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class WorkStatusRequest {

    private Integer statusId;

    @NotBlank(message = "Status name is required")
    private String statusName;

}

package com.hospital.Arogeva.payload;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class RoleRequest {

    private Integer roleId;

    @NotBlank(message = "Role name is required")
    private String roleName;

    private String description;

}

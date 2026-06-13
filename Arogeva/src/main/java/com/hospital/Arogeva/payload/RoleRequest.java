package com.hospital.Arogeva.payload;

import lombok.Data;

@Data
public class RoleRequest {

    private Integer roleId;
    private String roleName;
    private String description;

}

package com.hospital.Arogeva.service;

import com.hospital.Arogeva.payload.RoleRequest;
import com.hospital.Arogeva.payload.RoleResponse;
import java.util.List;

public interface RoleService {
    

    RoleResponse createOrUpdateRole(RoleRequest request);

    List<RoleResponse> getAllRoles();
    

    RoleResponse getRoleById(Integer roleId);
    

    RoleResponse getRoleByName(String roleName);
    

    RoleResponse deleteRole(Integer roleId);
}

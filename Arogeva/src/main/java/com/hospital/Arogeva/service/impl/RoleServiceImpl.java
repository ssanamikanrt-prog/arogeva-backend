package com.hospital.Arogeva.service.impl;

import com.hospital.Arogeva.entity.Role;
import com.hospital.Arogeva.payload.RoleRequest;
import com.hospital.Arogeva.payload.RoleResponse;
import com.hospital.Arogeva.repository.RoleRepository;
import com.hospital.Arogeva.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public RoleResponse createOrUpdateRole(RoleRequest request) {
        RoleResponse response = new RoleResponse();
        
        try {
            Role role;
            

            if (request.getRoleId() != null) {
                Optional<Role> existing = roleRepository.findById(request.getRoleId());
                
                if (existing.isPresent()) {

                    role = existing.get();
                    role.setRoleName(request.getRoleName());
                    role.setDescription(request.getDescription());
                    role.setUpdatedAt(LocalDateTime.now());
                    roleRepository.save(role);
                    response.setSuccess(true);
                    response.setMessage("Role updated successfully");
                } else {
                    response.setSuccess(false);
                    response.setMessage("Role with ID " + request.getRoleId() + " not found");
                    return response;
                }
            } else {
                role = new Role();
                role.setRoleName(request.getRoleName());
                role.setDescription(request.getDescription());
                role.setCreatedAt(LocalDateTime.now());
                roleRepository.save(role);
                response.setSuccess(true);
                response.setMessage("Role created successfully");
            }
            

            response.setRoleId(role.getRoleId());
            response.setRoleName(role.getRoleName());
            response.setDescription(role.getDescription());
            response.setCreatedAt(role.getCreatedAt());
            response.setUpdatedAt(role.getUpdatedAt());
            
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Error: " + e.getMessage());
        }
        
        return response;
    }

    @Override
    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RoleResponse getRoleById(Integer roleId) {
        Optional<Role> role = roleRepository.findById(roleId);
        
        if (role.isPresent()) {
            RoleResponse response = convertToResponse(role.get());
            response.setSuccess(true);
            return response;
        }
        
        RoleResponse response = new RoleResponse();
        response.setSuccess(false);
        response.setMessage("Role with ID " + roleId + " not found");
        return response;
    }

    @Override
    public RoleResponse getRoleByName(String roleName) {
        Optional<Role> role = roleRepository.findByRoleName(roleName);
        
        if (role.isPresent()) {
            RoleResponse response = convertToResponse(role.get());
            response.setSuccess(true);
            return response;
        }
        
        RoleResponse response = new RoleResponse();
        response.setSuccess(false);
        response.setMessage("Role with name " + roleName + " not found");
        return response;
    }

    @Override
    public RoleResponse deleteRole(Integer roleId) {
        RoleResponse response = new RoleResponse();
        
        try {
            Optional<Role> role = roleRepository.findById(roleId);
            
            if (role.isPresent()) {
                roleRepository.deleteById(roleId);
                response.setSuccess(true);
                response.setMessage("Role deleted successfully");
            } else {
                response.setSuccess(false);
                response.setMessage("Role with ID " + roleId + " not found");
            }
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Error: " + e.getMessage());
        }
        
        return response;
    }

    private RoleResponse convertToResponse(Role role) {
        RoleResponse response = new RoleResponse();
        response.setRoleId(role.getRoleId());
        response.setRoleName(role.getRoleName());
        response.setDescription(role.getDescription());
        response.setCreatedAt(role.getCreatedAt());
        response.setUpdatedAt(role.getUpdatedAt());
        response.setSuccess(true);
        return response;
    }
}

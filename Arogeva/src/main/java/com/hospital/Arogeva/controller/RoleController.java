package com.hospital.Arogeva.controller;

import com.hospital.Arogeva.advices.ApiResponse;

import com.hospital.Arogeva.payload.RoleRequest;
import com.hospital.Arogeva.payload.RoleResponse;
import com.hospital.Arogeva.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "*")
@Tag(name = "Roles", description = "Endpoints for managing Roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Operation(summary = "Create or Update Role", description = "Creates a new Role if roleId is not provided, or updates existing if ID is provided")
    @PostMapping("/create-or-update")
    public ResponseEntity<ApiResponse<RoleResponse>> createOrUpdateRole(@Valid @RequestBody RoleRequest request) {

        RoleResponse response = roleService.createOrUpdateRole(request);

        return ResponseEntity.ok(new ApiResponse<>(response));

    }

    @Operation(summary = "Get All Roles", description = "Fetches all available Roles")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<RoleResponse>>> getAllRoles() {

        List<RoleResponse> response = roleService.getAllRoles();

        return ResponseEntity.ok(new ApiResponse<>(response));

    }

    @Operation(summary = "Get Role By ID", description = "Fetches a specific Role by its ID")
    @GetMapping("/{roleId}")
    public ResponseEntity<ApiResponse<RoleResponse>> getRoleById(@PathVariable Integer roleId) {

        RoleResponse response = roleService.getRoleById(roleId);

        return ResponseEntity.ok(new ApiResponse<>(response));

    }

    @Operation(summary = "Get Role By Name", description = "Fetches a specific Role by its name")
    @GetMapping("/name/{roleName}")
    public ResponseEntity<ApiResponse<RoleResponse>> getRoleByName(@PathVariable String roleName) {

        RoleResponse response = roleService.getRoleByName(roleName);

        return ResponseEntity.ok(new ApiResponse<>(response));

    }

    @Operation(summary = "Delete Role", description = "Deletes a Role by its ID")
    @DeleteMapping("/{roleId}")
    public ResponseEntity<ApiResponse<RoleResponse>> deleteRole(@PathVariable Integer roleId) {

        RoleResponse response = roleService.deleteRole(roleId);

        return ResponseEntity.ok(new ApiResponse<>(response));

    }
}

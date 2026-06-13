package com.hospital.Arogeva.controller;

import com.hospital.Arogeva.advices.ApiResponse;

import com.hospital.Arogeva.payload.LoginRequest;
import com.hospital.Arogeva.payload.LoginResponse;
import com.hospital.Arogeva.payload.UserResponse;
import com.hospital.Arogeva.payload.CreateUserRequest;
import com.hospital.Arogeva.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@Tag(name = "Authentication", description = "Endpoints for user login and fetching user lists")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(summary = "Get Users", description = "Fetches all active resources and their roles for the login dropdown")
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getUsersForDropdown() {
        return ResponseEntity.ok(new ApiResponse<>(authService.getAllUsersForDropdown()));
    }

    @Operation(summary = "Authenticate User", description = "Verifies the provided user ID and PIN to authenticate a user")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.authenticate(loginRequest);
        return ResponseEntity.ok(new ApiResponse<>(response));
    }

    @Operation(summary = "Create User", description = "Creates a new user and encrypts the password")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<LoginResponse>> registerUser(@RequestBody CreateUserRequest request) {
        LoginResponse response = authService.createUser(request);
        return ResponseEntity.ok(new ApiResponse<>(response));
    }

}

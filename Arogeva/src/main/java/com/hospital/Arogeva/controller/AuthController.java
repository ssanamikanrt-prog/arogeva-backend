package com.hospital.Arogeva.controller;

import com.hospital.Arogeva.advices.ApiResponse;

import com.hospital.Arogeva.payload.LoginRequest;
import com.hospital.Arogeva.payload.LoginResponse;
import com.hospital.Arogeva.payload.UserResponse;
import com.hospital.Arogeva.payload.CreateUserRequest;
import com.hospital.Arogeva.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

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
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.authenticate(loginRequest);
        if (response.isSuccess() && response.getRefreshToken() != null) {
            ResponseCookie cookie = ResponseCookie.from("refresh_token", response.getRefreshToken())
                    .httpOnly(true)
                    .secure(true) // Should be true in production with HTTPS
                    .path("/api/auth")
                    .maxAge(7 * 24 * 60 * 60)
                    .sameSite("Strict")
                    .build();
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(new ApiResponse<>(response));
        }
        return ResponseEntity.ok(new ApiResponse<>(response));
    }

    @Operation(summary = "Create User", description = "Creates a new user and encrypts the password")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<LoginResponse>> registerUser(@Valid @RequestBody CreateUserRequest request) {
        LoginResponse response = authService.createUser(request);
        return ResponseEntity.ok(new ApiResponse<>(response));
    }

    @Operation(summary = "Refresh Token", description = "Generates a new access token using the refresh token cookie")
    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(@CookieValue(name = "refresh_token", required = false) String refreshToken) {
        LoginResponse response = authService.refreshToken(refreshToken);
        if (!response.isSuccess()) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(response));
        }
        return ResponseEntity.ok(new ApiResponse<>(response));
    }

    @Operation(summary = "Logout", description = "Clears the refresh token cookie to log the user out")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout() {
        ResponseCookie cookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(true)
                .path("/api/auth")
                .maxAge(0) // delete cookie
                .sameSite("Strict")
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new ApiResponse<>("Logged out successfully"));
    }
}

package com.hospital.Arogeva.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private boolean success;
    private String message;
    private UserResponse user;
    private String token;

    @JsonIgnore
    private String refreshToken;

    public LoginResponse(boolean success, String message, UserResponse user) {
        this.success = success;
        this.message = message;
        this.user = user;
    }

    public LoginResponse(boolean success, String message, UserResponse user, String token) {
        this.success = success;
        this.message = message;
        this.user = user;
        this.token = token;
    }

}

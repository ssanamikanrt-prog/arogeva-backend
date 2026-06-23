package com.hospital.Arogeva.payload;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.hospital.Arogeva.enums.AppRole;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class CreateUserRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Must be a valid email format")
    private String email;

    @NotBlank(message = "Full name is required")
    private String fullName;

    private String userId;

    @NotBlank(message = "Mobile number is required")
    private String mobileNumber;

    @NotBlank(message = "Password is required")
    private String password;

}
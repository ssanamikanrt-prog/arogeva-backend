package com.hospital.Arogeva.payload;

import lombok.Data;

@Data
public class CreateUserRequest {

    private String email;
    private String fullName;
    private String userId;
    private String mobileNumber;
    private String password;
    private String roleName;


}
package com.hospital.Arogeva.payload;

import lombok.Data;

@Data
public class LoginRequest {

    private String email;
  //  private String pin;
    private String password;

}

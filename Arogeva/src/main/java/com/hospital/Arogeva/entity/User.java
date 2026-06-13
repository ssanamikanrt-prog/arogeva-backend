package com.hospital.Arogeva.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "user_id")
//    private Integer userId;
//
//    @Column(name = "employee_code", length = 50)
//    private String employeeCode;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sl_no")
    private Integer slNo;

    @Column(name = "user_id", length = 50, unique = true)
    private String userId;


    @Column(name = "full_name", length = 100)
    private String fullName;

    @Column(name = "email", length = 150, unique = true)
    private String email;

    @Column(name = "mobile_number", length = 15)
    private String mobileNumber;

    @Column(name = "password", length = 255)
    private String password;

    @Column(name = "original_password", length = 255)
    private String originalPassword;

    @Column(name = "is_active")
    private String isActive;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}

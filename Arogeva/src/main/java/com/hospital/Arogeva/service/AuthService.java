package com.hospital.Arogeva.service;

import com.hospital.Arogeva.advices.ApiResponse;
import com.hospital.Arogeva.entity.Resource;
import com.hospital.Arogeva.entity.User;
import com.hospital.Arogeva.enums.AppRole;
import com.hospital.Arogeva.payload.LoginRequest;
import com.hospital.Arogeva.payload.LoginResponse;
import com.hospital.Arogeva.payload.UserResponse;
import com.hospital.Arogeva.repository.ResourceRepository;
import com.hospital.Arogeva.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hospital.Arogeva.entity.Role;
import com.hospital.Arogeva.entity.UserRole;
import com.hospital.Arogeva.entity.DeveloperMaster;
import com.hospital.Arogeva.entity.Resource;
import com.hospital.Arogeva.payload.CreateUserRequest;
import com.hospital.Arogeva.repository.RoleRepository;
import com.hospital.Arogeva.repository.UserRoleRepository;
import com.hospital.Arogeva.repository.DeveloperMasterRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Map;
import com.hospital.Arogeva.security.JwtTokenProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;

@Service
public class AuthService {

    private static final String STATIC_SALT = "NRT";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private DeveloperMasterRepository developerMasterRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    public List<UserResponse> getAllUsersForDropdown() {

        List<User> users = userRepository.findAll()
                .stream()
                .filter(u -> "true".equalsIgnoreCase(u.getIsActive()) || "Y".equalsIgnoreCase(u.getIsActive()) || "1".equals(u.getIsActive()))
                .collect(Collectors.toList());


        List<Resource> resources = resourceRepository.findAllWithDetails();
        Map<String, String> devTypeMap = new HashMap<>();
        for (Resource r : resources) {
            if (r.getUser() != null && r.getDeveloperName() != null) {
                devTypeMap.put(r.getUser().getUserId(), r.getDeveloperName().getDeveloperName());
            }
        }


        List<UserRole> userRoles = userRoleRepository.findAll();
        Map<String, String> roleMap = new HashMap<>();
        for (UserRole ur : userRoles) {
            if (ur.getUser() != null && ur.getRole() != null) {
                roleMap.put(ur.getUser().getUserId(), ur.getRole().getRoleName());
            }
        }

        return users.stream().map(u -> {
            String roleName = roleMap.getOrDefault(u.getUserId(), "UNKNOWN");
            String devType = devTypeMap.get(u.getUserId());
            return new UserResponse(
                    u.getUserId(),
                    u.getFullName(),
                    u.getEmail(),
                    devType,
                    roleName
            );
        }).collect(Collectors.toList());
    }

    public List<UserResponse> getManagersForDropdown() {
        return getAllUsersForDropdown().stream()
                .filter(u -> "ROLE_MANAGER".equals(u.getRoleName()))
                .collect(Collectors.toList());
    }



    public LoginResponse authenticate(LoginRequest request) {
        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            return new LoginResponse(false, "Email is required", null);
        }

        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Fetch role for response and authorization check
            String roleName = userRoleRepository.findAll().stream()
                    .filter(ur -> ur.getUser() != null && ur.getUser().getUserId().equals(user.getUserId()))
                    .map(ur -> ur.getRole() != null ? ur.getRole().getRoleName() : "UNKNOWN")
                    .findFirst().orElse("UNKNOWN");


            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                return new LoginResponse(false, "PIN is required", null);
            }
            String saltedPin = request.getPassword() + STATIC_SALT;
            if (!passwordEncoder.matches(saltedPin, user.getPassword())) {
                return new LoginResponse(false, "Invalid PIN", null);
            }

            //authentication is successful
            if ("true".equalsIgnoreCase(user.getIsActive()) || "Y".equalsIgnoreCase(user.getIsActive()) || "1".equals(user.getIsActive())) {


                String devType = resourceRepository.findAllWithDetails().stream()
                        .filter(r -> r.getUser() != null && r.getUser().getUserId().equals(user.getUserId()))
                        .filter(r -> r.getDeveloperName() != null && r.getDeveloperName().getDeveloperName() != null)
                        .map(r -> r.getDeveloperName().getDeveloperName())
                        .findFirst().orElse(null);

                UserResponse ur = new UserResponse(user.getUserId(), user.getFullName(), user.getEmail(), devType, roleName);
                

                String accessToken = tokenProvider.generateAccessToken(user.getUserId());
                String refreshToken = tokenProvider.generateRefreshToken(user.getUserId());
                
                return new LoginResponse(true, "Login successful", ur, accessToken, refreshToken);
            } else {
                return new LoginResponse(false, "User is inactive", null);
            }
        }
        return new LoginResponse(false, "User not found", null);
    }

    public LoginResponse createUser(CreateUserRequest request, AppRole roleName) {
        try {

            if (request.getEmail() != null && !request.getEmail().isEmpty()) {
                Optional<User> existingEmail = userRepository.findByEmail(request.getEmail());
                if (existingEmail.isPresent()) {
                    return new LoginResponse(false, "Email already exists. Please use a different email.", null);
                }
            }

            if (request.getUserId() != null && !request.getUserId().isEmpty()) {
                Optional<User> existingUserId = userRepository.findByUserId(request.getUserId());
                if (existingUserId.isPresent()) {
                    return new LoginResponse(false, "User ID already exists. Please use a different user ID.", null);
                }
            }

            Role roleToAssign = null;
            if (roleName != null) {
                Optional<Role> roleOpt = roleRepository.findByRoleName(roleName.name());
                if (!roleOpt.isPresent()) {
                    return new LoginResponse(false, "Invalid role name. Please provide an existing role.", null);
                }
                roleToAssign = roleOpt.get();
            }

            User user = new User();
            user.setFullName(request.getFullName());
            user.setUserId(request.getUserId());
            user.setEmail(request.getEmail());
            user.setMobileNumber(request.getMobileNumber());
            

            String saltedPassword = request.getPassword() + STATIC_SALT;
            String saltedHashedPassword = passwordEncoder.encode(saltedPassword);
            user.setPassword(saltedHashedPassword);
            user.setOriginalPassword(request.getPassword());

            user.setIsActive("true");
            user.setCreatedAt(LocalDateTime.now());
            
            user = userRepository.save(user);

            if (roleToAssign != null) {
                UserRole userRole = new UserRole();
                userRole.setUser(user);
                userRole.setRole(roleToAssign);
                userRole.setAssignedAt(LocalDateTime.now());
                userRoleRepository.save(userRole);
            }

            UserResponse ur = new UserResponse(user.getUserId(), user.getFullName(), user.getEmail(), null, roleName != null ? roleName.name() : "UNKNOWN");
            return new LoginResponse(true, "User created successfully", ur);
        } catch (Exception e) {
            e.printStackTrace();
            return new LoginResponse(false, "Error creating user: " + e.getMessage(), null);
        }
    }
    public LoginResponse refreshToken(String refreshToken) {
        if (refreshToken == null || !tokenProvider.validateToken(refreshToken)) {
            return new LoginResponse(false, "Invalid or expired refresh token", null);
        }

        String userId = tokenProvider.getUserIdFromJWT(refreshToken);
        Optional<User> userOpt = userRepository.findByUserId(userId);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if ("true".equalsIgnoreCase(user.getIsActive()) || "Y".equalsIgnoreCase(user.getIsActive()) || "1".equals(user.getIsActive())) {
                String newAccessToken = tokenProvider.generateAccessToken(user.getUserId());
                return new LoginResponse(true, "Token refreshed successfully", null, newAccessToken);
            }
        }
        return new LoginResponse(false, "User not found or inactive", null);
    }

}

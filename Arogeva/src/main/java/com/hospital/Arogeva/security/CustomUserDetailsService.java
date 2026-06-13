package com.hospital.Arogeva.security;

import com.hospital.Arogeva.entity.User;
import com.hospital.Arogeva.entity.UserRole;
import com.hospital.Arogeva.repository.UserRepository;
import com.hospital.Arogeva.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;




import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userIdStr) throws UsernameNotFoundException {
        try {
            User user = userRepository.findByUserId(userIdStr)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userIdStr));

            List<GrantedAuthority> authorities = userRoleRepository.findAll().stream()
                    .filter(ur -> {
                        try {
                            return ur.getUser() != null && user.getUserId().equals(ur.getUser().getUserId());
                        } catch (Exception e) {
                            return false;
                        }
                    })
                    .map(ur -> ur.getRole() != null ? new SimpleGrantedAuthority(ur.getRole().getRoleName()) : new SimpleGrantedAuthority("UNKNOWN"))
                    .collect(Collectors.toList());

            if (authorities.isEmpty()) {
                authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
            }

            return new org.springframework.security.core.userdetails.User(
                    user.getUserId(),
                    user.getPassword() != null ? user.getPassword() : "",
                    authorities
            );
        } catch (Exception e) {
            throw new UsernameNotFoundException("Error loading user: " + e.getMessage());
        }
    }
}

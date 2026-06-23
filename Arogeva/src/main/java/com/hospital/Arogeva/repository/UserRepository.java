package com.hospital.Arogeva.repository;

import com.hospital.Arogeva.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {






    Optional<User> findByUserId(String userId);

    Optional<User> findByEmail(String email);

    Optional<User> findByFullName(String fullName);

}

package com.hospital.Arogeva.repository;

import com.hospital.Arogeva.entity.DeveloperMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeveloperMasterRepository extends JpaRepository<DeveloperMaster, Integer> {


    Optional<DeveloperMaster> findByDeveloperName(String developerName);


}

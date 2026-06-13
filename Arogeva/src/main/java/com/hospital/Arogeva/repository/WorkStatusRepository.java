package com.hospital.Arogeva.repository;
import com.hospital.Arogeva.entity.WorkStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkStatusRepository extends JpaRepository<WorkStatus, Integer> {}

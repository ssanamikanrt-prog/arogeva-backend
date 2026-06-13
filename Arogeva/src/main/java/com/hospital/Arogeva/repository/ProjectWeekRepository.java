package com.hospital.Arogeva.repository;
import com.hospital.Arogeva.entity.ProjectWeek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectWeekRepository extends JpaRepository<ProjectWeek, Integer> {}

package com.hospital.Arogeva.repository;
import com.hospital.Arogeva.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Integer> {
    List<Module> findByProject_ProjectId(Integer projectId);
}

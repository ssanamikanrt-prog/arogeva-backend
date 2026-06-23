package com.hospital.Arogeva.repository;
import com.hospital.Arogeva.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {


    List<Project> findByStatus(String status);
    
    List<Project> findByProjectManager(String projectManager);

    List<Project> findByProjectManagerId(String projectManagerId);

    @Query("SELECT DISTINCT p.architecture FROM Project p WHERE p.architecture IS NOT NULL")
    List<String> findDistinctArchitectures();

    @Query("SELECT DISTINCT p.projectManager FROM Project p WHERE p.projectManager IS NOT NULL AND TRIM(p.projectManager) <> ''")
    List<String> findDistinctProjectManagers();

    @Query("SELECT DISTINCT p.projectName FROM Project p WHERE p.projectName IS NOT NULL AND TRIM(p.projectName) <> ''")
    List<String> findDistinctProjectNames();

}

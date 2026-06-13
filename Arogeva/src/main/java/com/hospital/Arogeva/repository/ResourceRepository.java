package com.hospital.Arogeva.repository;

import com.hospital.Arogeva.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Integer> {



    @Query("SELECT r FROM Resource r " +
           "LEFT JOIN FETCH r.user " +
           "LEFT JOIN FETCH r.developerName " +
           "LEFT JOIN FETCH r.module")
    List<Resource> findAllWithDetails();

    @Query("SELECT r FROM Resource r " +
           "LEFT JOIN FETCH r.user " +
           "LEFT JOIN FETCH r.developerName " +
           "LEFT JOIN FETCH r.module " +
           "WHERE r.project.projectId = :projectId")
    List<Resource> findByProjectIdWithDetails(@Param("projectId") Integer projectId);


}

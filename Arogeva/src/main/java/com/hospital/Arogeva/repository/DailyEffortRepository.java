package com.hospital.Arogeva.repository;

import com.hospital.Arogeva.entity.DailyEffortEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DailyEffortRepository extends JpaRepository<DailyEffortEntry, Integer> {


    List<DailyEffortEntry> findByResource_ResourceIdAndWorkDate(Integer resourceId, LocalDate workDate);

    @Query("SELECT d FROM DailyEffortEntry d " +
           "JOIN FETCH d.week w " +
           "JOIN FETCH d.resource r " +
           "LEFT JOIN FETCH r.user u " +
           "LEFT JOIN FETCH r.developerName dev " +
           "LEFT JOIN FETCH d.module m " +
           "WHERE w.weekId = :weekId")
    List<DailyEffortEntry> findByWeekIdWithDetails(@Param("weekId") Integer weekId);

    @Query("SELECT d FROM DailyEffortEntry d " +
           "JOIN FETCH d.week w " +
           "JOIN FETCH d.resource r " +
           "LEFT JOIN FETCH r.user u " +
           "LEFT JOIN FETCH r.developerName dev " +
           "LEFT JOIN FETCH d.module m " +
           "LEFT JOIN FETCH d.activity act " +
           "LEFT JOIN FETCH d.status stat")
    List<DailyEffortEntry> findAllWithDetails();

    @Query("SELECT d FROM DailyEffortEntry d " +
           "JOIN FETCH d.week w " +
           "JOIN FETCH d.resource r " +
           "LEFT JOIN FETCH r.user u " +
           "LEFT JOIN FETCH r.developerName dev " +
           "LEFT JOIN FETCH d.module m " +
           "LEFT JOIN FETCH d.activity act " +
           "LEFT JOIN FETCH d.status stat " +
           "WHERE d.project.projectId = :projectId")
    List<DailyEffortEntry> findByProjectIdWithDetails(@Param("projectId") Integer projectId);

    @Query("SELECT d FROM DailyEffortEntry d " +
           "JOIN FETCH d.week w " +
           "JOIN FETCH d.resource r " +
           "LEFT JOIN FETCH r.user u " +
           "LEFT JOIN FETCH r.developerName dev " +
           "LEFT JOIN FETCH d.module m " +
           "LEFT JOIN FETCH d.activity act " +
           "LEFT JOIN FETCH d.status stat " +
           "WHERE r.resourceId = :resourceId")
    List<DailyEffortEntry> findByResourceIdWithDetails(@Param("resourceId") Integer resourceId);

    @Query("SELECT d FROM DailyEffortEntry d " +
           "JOIN FETCH d.week w " +
           "JOIN FETCH d.resource r " +
           "LEFT JOIN FETCH r.user u " +
           "LEFT JOIN FETCH r.developerName dev " +
           "LEFT JOIN FETCH d.module m " +
           "LEFT JOIN FETCH d.activity act " +
           "LEFT JOIN FETCH d.status stat " +
           "LEFT JOIN FETCH d.createdBy cb " +
           "WHERE w.weekId = :weekId " +
           "AND cb.userId = :userId " +
           "ORDER BY d.workDate DESC")
    List<DailyEffortEntry> findByWeekIdAndUserIdForLogs(@Param("weekId") Integer weekId, @Param("userId") String userId);
}


package com.ahamo.dummy.demo2.application.repository;

import com.ahamo.dummy.demo2.application.entity.ApplicationStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationStepRepository extends JpaRepository<ApplicationStep, Long> {
    
    @Query("SELECT s FROM ApplicationStep s WHERE s.application.id = :applicationId ORDER BY s.stepNumber")
    List<ApplicationStep> findByApplicationIdOrderByStepNumber(@Param("applicationId") Long applicationId);
    
    @Query("SELECT s FROM ApplicationStep s WHERE s.application.applicationId = :applicationId ORDER BY s.stepNumber")
    List<ApplicationStep> findByApplicationApplicationIdOrderByStepNumber(@Param("applicationId") String applicationId);
    
    @Query("SELECT s FROM ApplicationStep s WHERE s.application.id = :applicationId AND s.stepNumber = :stepNumber")
    Optional<ApplicationStep> findByApplicationIdAndStepNumber(@Param("applicationId") Long applicationId, @Param("stepNumber") Integer stepNumber);
}

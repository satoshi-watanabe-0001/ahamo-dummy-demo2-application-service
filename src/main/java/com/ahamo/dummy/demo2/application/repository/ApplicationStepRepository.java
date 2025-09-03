package com.ahamo.dummy.demo2.application.repository;

import com.ahamo.dummy.demo2.application.entity.ApplicationStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationStepRepository extends JpaRepository<ApplicationStep, Long> {
    
    List<ApplicationStep> findByApplicationApplicationIdOrderByStepNumberAsc(String applicationId);
}

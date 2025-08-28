package com.ahamo.dummy.demo2.application.repository;

import com.ahamo.dummy.demo2.application.entity.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    
    Optional<Application> findByApplicationId(String applicationId);
    
    @Query("SELECT a FROM Application a WHERE a.email = :email ORDER BY a.createdAt DESC")
    Page<Application> findByEmailOrderByCreatedAtDesc(@Param("email") String email, Pageable pageable);
    
    @Query("SELECT a FROM Application a WHERE a.status = :status ORDER BY a.createdAt DESC")
    Page<Application> findByStatusOrderByCreatedAtDesc(@Param("status") Application.ApplicationStatus status, Pageable pageable);
    
    @Query("SELECT COUNT(a) FROM Application a WHERE a.status = :status")
    Long countByStatus(@Param("status") Application.ApplicationStatus status);
}

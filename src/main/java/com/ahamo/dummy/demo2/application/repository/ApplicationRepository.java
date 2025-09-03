package com.ahamo.dummy.demo2.application.repository;

import com.ahamo.dummy.demo2.application.entity.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, String> {
    
    Page<Application> findByEmail(String email, Pageable pageable);
}

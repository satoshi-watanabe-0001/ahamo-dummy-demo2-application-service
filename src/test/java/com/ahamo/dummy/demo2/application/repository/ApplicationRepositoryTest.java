package com.ahamo.dummy.demo2.application.repository;

import com.ahamo.dummy.demo2.application.entity.Application;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ApplicationRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Test
    void findByEmail_Success() {
        Application application = new Application();
        application.setApplicationId("APP-TEST-001");
        application.setEmail("test@example.com");
        application.setUserName("テストユーザー");
        application.setPhone("090-1234-5678");
        application.setSelectedPlan("ahamo-20gb");
        application.setSelectedDevice("iphone-15");
        application.setStatus(Application.ApplicationStatus.DRAFT);
        application.setCurrentStep(1);

        entityManager.persistAndFlush(application);

        Page<Application> result = applicationRepository.findByEmail("test@example.com", PageRequest.of(0, 10));

        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
        assertEquals("APP-TEST-001", result.getContent().get(0).getApplicationId());
    }

    @Test
    void findByEmail_NotFound() {
        Page<Application> result = applicationRepository.findByEmail("nonexistent@example.com", PageRequest.of(0, 10));

        assertTrue(result.isEmpty());
        assertEquals(0, result.getTotalElements());
    }
}

package com.ahamo.dummy.demo2.application.service;

import com.ahamo.dummy.demo2.application.dto.ApplicationRequest;
import com.ahamo.dummy.demo2.application.dto.ApplicationResponse;
import com.ahamo.dummy.demo2.application.entity.Application;
import com.ahamo.dummy.demo2.application.repository.ApplicationRepository;
import com.ahamo.dummy.demo2.application.repository.ApplicationStepRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private ApplicationStepRepository applicationStepRepository;

    @InjectMocks
    private ApplicationService applicationService;

    @Test
    void createApplication_Success() {
        ApplicationRequest request = new ApplicationRequest();
        request.setEmail("test@example.com");
        request.setUserName("テストユーザー");
        request.setPhone("090-1234-5678");
        request.setSelectedPlan("ahamo-20gb");
        request.setSelectedDevice("iphone-15");

        Application savedApplication = new Application();
        savedApplication.setApplicationId("APP-12345678");
        savedApplication.setEmail("test@example.com");
        savedApplication.setUserName("テストユーザー");
        savedApplication.setPhone("090-1234-5678");
        savedApplication.setSelectedPlan("ahamo-20gb");
        savedApplication.setSelectedDevice("iphone-15");
        savedApplication.setStatus(Application.ApplicationStatus.DRAFT);
        savedApplication.setCurrentStep(1);
        savedApplication.setCreatedAt(LocalDateTime.now());
        savedApplication.setUpdatedAt(LocalDateTime.now());

        when(applicationRepository.save(any(Application.class))).thenReturn(savedApplication);

        ApplicationResponse response = applicationService.createApplication(request);

        assertNotNull(response);
        assertEquals("test@example.com", response.getEmail());
        assertEquals("テストユーザー", response.getUserName());
        assertEquals("DRAFT", response.getStatus());
        assertEquals(1, response.getCurrentStep());
    }

    @Test
    void getApplication_Success() {
        Application application = new Application();
        application.setApplicationId("APP-12345678");
        application.setEmail("test@example.com");
        application.setUserName("テストユーザー");
        application.setStatus(Application.ApplicationStatus.DRAFT);
        application.setCurrentStep(1);

        when(applicationRepository.findById("APP-12345678")).thenReturn(Optional.of(application));

        Optional<ApplicationResponse> response = applicationService.getApplication("APP-12345678");

        assertTrue(response.isPresent());
        assertEquals("APP-12345678", response.get().getApplicationId());
        assertEquals("test@example.com", response.get().getEmail());
    }

    @Test
    void getApplication_NotFound() {
        when(applicationRepository.findById("non-existent-id")).thenReturn(Optional.empty());

        Optional<ApplicationResponse> response = applicationService.getApplication("non-existent-id");

        assertFalse(response.isPresent());
    }
}

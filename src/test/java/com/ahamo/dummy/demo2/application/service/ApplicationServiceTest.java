package com.ahamo.dummy.demo2.application.service;

import com.ahamo.dummy.demo2.application.entity.Application;
import com.ahamo.dummy.demo2.application.entity.ApplicationStep;
import com.ahamo.dummy.demo2.application.repository.ApplicationRepository;
import com.ahamo.dummy.demo2.application.repository.ApplicationStepRepository;
import com.ahamo.dummy.demo2.application.dto.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private ApplicationStepRepository applicationStepRepository;

    @InjectMocks
    private ApplicationService applicationService;

    @Test
    void createApplication_ShouldReturnApplicationResponse() {
        ApplicationRequest request = new ApplicationRequest(
            "田中太郎",
            "tanaka@example.com",
            "090-1234-5678",
            "30GBプラン",
            "iPhone 15",
            "通話オプション"
        );

        Application savedApplication = new Application();
        savedApplication.setId(1L);
        savedApplication.setApplicationId("APP-12345678");
        savedApplication.setUserName("田中太郎");
        savedApplication.setEmail("tanaka@example.com");
        savedApplication.setPhone("090-1234-5678");
        savedApplication.setSelectedPlan("30GBプラン");
        savedApplication.setSelectedDevice("iPhone 15");
        savedApplication.setSelectedOptions("通話オプション");
        savedApplication.setStatus(Application.ApplicationStatus.DRAFT);
        savedApplication.setCurrentStep(1);
        savedApplication.setTotalSteps(5);
        savedApplication.setEstimatedCompletionTime("約15分");
        savedApplication.setCreatedAt(LocalDateTime.now());
        savedApplication.setUpdatedAt(LocalDateTime.now());

        when(applicationRepository.save(any(Application.class))).thenReturn(savedApplication);

        ApplicationResponse result = applicationService.createApplication(request);

        assertThat(result).isNotNull();
        assertThat(result.getApplicationId()).isEqualTo("APP-12345678");
        assertThat(result.getUserName()).isEqualTo("田中太郎");
        assertThat(result.getEmail()).isEqualTo("tanaka@example.com");
        assertThat(result.getStatus()).isEqualTo(Application.ApplicationStatus.DRAFT);
        assertThat(result.getCurrentStep()).isEqualTo(1);
        assertThat(result.getTotalSteps()).isEqualTo(5);

        verify(applicationStepRepository, times(5)).save(any(ApplicationStep.class));
    }

    @Test
    void getApplicationById_ExistingId_ShouldReturnApplication() {
        Application application = new Application();
        application.setId(1L);
        application.setApplicationId("APP-12345678");
        application.setUserName("田中太郎");
        application.setEmail("tanaka@example.com");
        application.setPhone("090-1234-5678");
        application.setStatus(Application.ApplicationStatus.DRAFT);
        application.setCurrentStep(2);
        application.setTotalSteps(5);
        application.setCreatedAt(LocalDateTime.now());
        application.setUpdatedAt(LocalDateTime.now());

        when(applicationRepository.findByApplicationId("APP-12345678")).thenReturn(Optional.of(application));

        ApplicationResponse result = applicationService.getApplicationById("APP-12345678");

        assertThat(result).isNotNull();
        assertThat(result.getApplicationId()).isEqualTo("APP-12345678");
        assertThat(result.getUserName()).isEqualTo("田中太郎");
        assertThat(result.getCurrentStep()).isEqualTo(2);
    }

    @Test
    void getApplicationById_NonExistingId_ShouldReturnNull() {
        when(applicationRepository.findByApplicationId("APP-NOTFOUND")).thenReturn(Optional.empty());

        ApplicationResponse result = applicationService.getApplicationById("APP-NOTFOUND");

        assertThat(result).isNull();
    }

    @Test
    void getApplicationsByEmail_ShouldReturnPagedApplications() {
        Application application = new Application();
        application.setId(1L);
        application.setApplicationId("APP-12345678");
        application.setUserName("田中太郎");
        application.setEmail("tanaka@example.com");
        application.setStatus(Application.ApplicationStatus.SUBMITTED);
        application.setCreatedAt(LocalDateTime.now());
        application.setUpdatedAt(LocalDateTime.now());

        Page<Application> applicationPage = new PageImpl<>(List.of(application));
        when(applicationRepository.findByEmailOrderByCreatedAtDesc(anyString(), any(Pageable.class)))
            .thenReturn(applicationPage);

        Page<ApplicationResponse> result = applicationService.getApplicationsByEmail("tanaka@example.com", 1, 10);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getApplicationId()).isEqualTo("APP-12345678");
        assertThat(result.getContent().get(0).getEmail()).isEqualTo("tanaka@example.com");
    }

    @Test
    void updateApplicationStep_ShouldUpdateStepAndApplication() {
        Application application = new Application();
        application.setId(1L);
        application.setApplicationId("APP-12345678");
        application.setCurrentStep(1);

        ApplicationStep step = new ApplicationStep();
        step.setId(1L);
        step.setApplication(application);
        step.setStepNumber(2);
        step.setStepName("プラン選択");
        step.setStatus(ApplicationStep.StepStatus.PENDING);

        ApplicationStepRequest request = new ApplicationStepRequest(
            2,
            "{\"planId\": \"30gb\"}"
        );

        when(applicationRepository.findByApplicationId("APP-12345678")).thenReturn(Optional.of(application));
        when(applicationStepRepository.findByApplicationIdAndStepNumber(1L, 2)).thenReturn(Optional.of(step));
        when(applicationStepRepository.save(any(ApplicationStep.class))).thenReturn(step);
        when(applicationRepository.save(any(Application.class))).thenReturn(application);

        ApplicationResponse result = applicationService.updateApplicationStep("APP-12345678", request);

        assertThat(result).isNotNull();
        assertThat(result.getApplicationId()).isEqualTo("APP-12345678");
        verify(applicationStepRepository).save(step);
        verify(applicationRepository).save(application);
    }

    @Test
    void updateApplicationStep_NonExistingApplication_ShouldThrowException() {
        ApplicationStepRequest request = new ApplicationStepRequest(
            2,
            "{\"planId\": \"30gb\"}"
        );

        when(applicationRepository.findByApplicationId("APP-NOTFOUND")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> applicationService.updateApplicationStep("APP-NOTFOUND", request))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("申し込みが見つかりません");
    }

    @Test
    void submitApplication_ShouldUpdateStatusToSubmitted() {
        Application application = new Application();
        application.setId(1L);
        application.setApplicationId("APP-12345678");
        application.setStatus(Application.ApplicationStatus.DRAFT);
        application.setCreatedAt(LocalDateTime.now());
        application.setUpdatedAt(LocalDateTime.now());

        when(applicationRepository.findByApplicationId("APP-12345678")).thenReturn(Optional.of(application));
        when(applicationRepository.save(any(Application.class))).thenReturn(application);

        ApplicationResponse result = applicationService.submitApplication("APP-12345678");

        assertThat(result).isNotNull();
        assertThat(result.getApplicationId()).isEqualTo("APP-12345678");
        assertThat(result.getStatus()).isEqualTo(Application.ApplicationStatus.SUBMITTED);
        verify(applicationRepository).save(application);
    }

    @Test
    void getApplicationSteps_ShouldReturnStepsList() {
        ApplicationStep step1 = new ApplicationStep();
        step1.setId(1L);
        step1.setStepNumber(1);
        step1.setStepName("基本情報入力");
        step1.setStatus(ApplicationStep.StepStatus.COMPLETED);
        step1.setCreatedAt(LocalDateTime.now());
        step1.setUpdatedAt(LocalDateTime.now());
        step1.setCompletedAt(LocalDateTime.now());

        ApplicationStep step2 = new ApplicationStep();
        step2.setId(2L);
        step2.setStepNumber(2);
        step2.setStepName("プラン選択");
        step2.setStatus(ApplicationStep.StepStatus.IN_PROGRESS);
        step2.setCreatedAt(LocalDateTime.now());
        step2.setUpdatedAt(LocalDateTime.now());

        when(applicationStepRepository.findByApplicationApplicationIdOrderByStepNumber("APP-12345678"))
            .thenReturn(List.of(step1, step2));

        List<ApplicationStepResponse> result = applicationService.getApplicationSteps("APP-12345678");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getStepNumber()).isEqualTo(1);
        assertThat(result.get(0).getStepName()).isEqualTo("基本情報入力");
        assertThat(result.get(0).getStatus()).isEqualTo(ApplicationStep.StepStatus.COMPLETED);
        assertThat(result.get(1).getStepNumber()).isEqualTo(2);
        assertThat(result.get(1).getStatus()).isEqualTo(ApplicationStep.StepStatus.IN_PROGRESS);
    }
}

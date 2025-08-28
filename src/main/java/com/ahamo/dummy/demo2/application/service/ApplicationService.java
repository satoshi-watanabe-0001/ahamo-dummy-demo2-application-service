package com.ahamo.dummy.demo2.application.service;

import com.ahamo.dummy.demo2.application.entity.Application;
import com.ahamo.dummy.demo2.application.entity.ApplicationStep;
import com.ahamo.dummy.demo2.application.repository.ApplicationRepository;
import com.ahamo.dummy.demo2.application.repository.ApplicationStepRepository;
import com.ahamo.dummy.demo2.application.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ApplicationService {
    
    private final ApplicationRepository applicationRepository;
    private final ApplicationStepRepository applicationStepRepository;
    
    public ApplicationResponse createApplication(ApplicationRequest request) {
        log.info("申し込み作成開始: email={}", request.getEmail());
        
        Application application = new Application();
        application.setApplicationId(generateApplicationId());
        application.setUserName(request.getUserName());
        application.setEmail(request.getEmail());
        application.setPhone(request.getPhone());
        application.setSelectedPlan(request.getSelectedPlan());
        application.setSelectedDevice(request.getSelectedDevice());
        application.setSelectedOptions(request.getSelectedOptions());
        application.setStatus(Application.ApplicationStatus.DRAFT);
        application.setCurrentStep(1);
        application.setTotalSteps(5);
        application.setEstimatedCompletionTime("約15分");
        
        Application savedApplication = applicationRepository.save(application);
        
        initializeApplicationSteps(savedApplication);
        
        log.info("申し込み作成完了: applicationId={}", savedApplication.getApplicationId());
        
        return convertToResponse(savedApplication);
    }
    
    @Transactional(readOnly = true)
    public ApplicationResponse getApplicationById(String applicationId) {
        log.info("申し込み詳細取得: applicationId={}", applicationId);
        
        Application application = applicationRepository.findByApplicationId(applicationId)
            .orElse(null);
        
        if (application == null) {
            log.warn("申し込みが見つかりません: applicationId={}", applicationId);
            return null;
        }
        
        return convertToResponse(application);
    }
    
    @Transactional(readOnly = true)
    public Page<ApplicationResponse> getApplicationsByEmail(String email, int page, int limit) {
        log.info("ユーザー申し込み一覧取得: email={}, page={}, limit={}", email, page, limit);
        
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Application> applications = applicationRepository.findByEmailOrderByCreatedAtDesc(email, pageable);
        
        return applications.map(this::convertToResponse);
    }
    
    public ApplicationResponse updateApplicationStep(String applicationId, ApplicationStepRequest request) {
        log.info("申し込みステップ更新: applicationId={}, stepNumber={}", applicationId, request.getStepNumber());
        
        Application application = applicationRepository.findByApplicationId(applicationId)
            .orElseThrow(() -> new RuntimeException("申し込みが見つかりません: " + applicationId));
        
        ApplicationStep step = applicationStepRepository.findByApplicationIdAndStepNumber(
            application.getId(), request.getStepNumber())
            .orElseThrow(() -> new RuntimeException("ステップが見つかりません: " + request.getStepNumber()));
        
        step.setStepData(request.getStepData());
        step.setStatus(ApplicationStep.StepStatus.COMPLETED);
        step.setCompletedAt(LocalDateTime.now());
        
        applicationStepRepository.save(step);
        
        if (request.getStepNumber() >= application.getCurrentStep()) {
            application.setCurrentStep(request.getStepNumber() + 1);
            applicationRepository.save(application);
        }
        
        log.info("申し込みステップ更新完了: applicationId={}, stepNumber={}", applicationId, request.getStepNumber());
        
        return convertToResponse(application);
    }
    
    public ApplicationResponse submitApplication(String applicationId) {
        log.info("申し込み提出: applicationId={}", applicationId);
        
        Application application = applicationRepository.findByApplicationId(applicationId)
            .orElseThrow(() -> new RuntimeException("申し込みが見つかりません: " + applicationId));
        
        application.setStatus(Application.ApplicationStatus.SUBMITTED);
        application.setSubmittedAt(LocalDateTime.now());
        
        Application savedApplication = applicationRepository.save(application);
        
        log.info("申し込み提出完了: applicationId={}", applicationId);
        
        return convertToResponse(savedApplication);
    }
    
    @Transactional(readOnly = true)
    public List<ApplicationStepResponse> getApplicationSteps(String applicationId) {
        log.info("申し込みステップ一覧取得: applicationId={}", applicationId);
        
        List<ApplicationStep> steps = applicationStepRepository.findByApplicationApplicationIdOrderByStepNumber(applicationId);
        
        return steps.stream()
            .map(this::convertStepToResponse)
            .collect(Collectors.toList());
    }
    
    private void initializeApplicationSteps(Application application) {
        String[] stepNames = {
            "基本情報入力",
            "プラン選択",
            "端末選択",
            "オプション選択",
            "確認・提出"
        };
        
        for (int i = 0; i < stepNames.length; i++) {
            ApplicationStep step = new ApplicationStep();
            step.setApplication(application);
            step.setStepNumber(i + 1);
            step.setStepName(stepNames[i]);
            step.setStatus(i == 0 ? ApplicationStep.StepStatus.IN_PROGRESS : ApplicationStep.StepStatus.PENDING);
            
            applicationStepRepository.save(step);
        }
    }
    
    private String generateApplicationId() {
        return "APP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    private ApplicationResponse convertToResponse(Application application) {
        return new ApplicationResponse(
            application.getApplicationId(),
            application.getUserName(),
            application.getEmail(),
            application.getPhone(),
            application.getSelectedPlan(),
            application.getSelectedDevice(),
            application.getSelectedOptions(),
            application.getStatus(),
            application.getStatus().getDisplayName(),
            application.getCurrentStep(),
            application.getTotalSteps(),
            application.getEstimatedCompletionTime(),
            application.getCreatedAt(),
            application.getUpdatedAt(),
            application.getSubmittedAt()
        );
    }
    
    private ApplicationStepResponse convertStepToResponse(ApplicationStep step) {
        return new ApplicationStepResponse(
            step.getId(),
            step.getStepNumber(),
            step.getStepName(),
            step.getStepData(),
            step.getStatus(),
            step.getStatus().getDisplayName(),
            step.getValidationErrors(),
            step.getCreatedAt(),
            step.getUpdatedAt(),
            step.getCompletedAt()
        );
    }
}

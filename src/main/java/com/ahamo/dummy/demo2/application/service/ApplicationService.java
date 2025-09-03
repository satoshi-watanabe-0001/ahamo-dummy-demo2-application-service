package com.ahamo.dummy.demo2.application.service;

import com.ahamo.dummy.demo2.application.dto.*;
import com.ahamo.dummy.demo2.application.entity.Application;
import com.ahamo.dummy.demo2.application.entity.ApplicationStep;
import com.ahamo.dummy.demo2.application.repository.ApplicationRepository;
import com.ahamo.dummy.demo2.application.repository.ApplicationStepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ApplicationStepRepository applicationStepRepository;

    public ApplicationResponse createApplication(ApplicationRequest request) {
        Application application = new Application();
        application.setApplicationId(generateApplicationId());
        application.setEmail(request.getEmail());
        application.setUserName(request.getUserName());
        application.setPhone(request.getPhone());
        application.setSelectedPlan(request.getSelectedPlan());
        application.setSelectedDevice(request.getSelectedDevice());
        application.setStatus(Application.ApplicationStatus.DRAFT);
        application.setCurrentStep(1);

        Application savedApplication = applicationRepository.save(application);
        return new ApplicationResponse(savedApplication);
    }

    @Transactional(readOnly = true)
    public Optional<ApplicationResponse> getApplication(String applicationId) {
        return applicationRepository.findById(applicationId)
                .map(ApplicationResponse::new);
    }

    @Transactional(readOnly = true)
    public ApplicationListResponse getApplications(String email, int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<Application> applicationPage = applicationRepository.findByEmail(email, pageable);
        
        List<ApplicationResponse> applications = applicationPage.getContent()
                .stream()
                .map(ApplicationResponse::new)
                .collect(Collectors.toList());

        return new ApplicationListResponse(
                applications,
                applicationPage.getTotalElements(),
                page,
                limit
        );
    }

    public ApplicationResponse updateApplicationStep(String applicationId, StepUpdateRequest request) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("申し込みが見つかりません"));

        ApplicationStep step = new ApplicationStep();
        step.setApplication(application);
        step.setStepNumber(request.getStepNumber());
        step.setStepData(request.getStepData());
        applicationStepRepository.save(step);

        application.setCurrentStep(request.getStepNumber() + 1);
        Application updatedApplication = applicationRepository.save(application);

        return new ApplicationResponse(updatedApplication);
    }

    public ApplicationResponse submitApplication(String applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("申し込みが見つかりません"));

        application.setStatus(Application.ApplicationStatus.SUBMITTED);
        Application updatedApplication = applicationRepository.save(application);

        return new ApplicationResponse(updatedApplication);
    }

    @Transactional(readOnly = true)
    public List<ApplicationStepResponse> getApplicationSteps(String applicationId) {
        List<ApplicationStep> steps = applicationStepRepository
                .findByApplicationApplicationIdOrderByStepNumberAsc(applicationId);
        
        return steps.stream()
                .map(ApplicationStepResponse::new)
                .collect(Collectors.toList());
    }

    private String generateApplicationId() {
        return "APP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}

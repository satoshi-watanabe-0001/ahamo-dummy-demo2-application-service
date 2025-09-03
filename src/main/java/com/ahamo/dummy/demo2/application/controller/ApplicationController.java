package com.ahamo.dummy.demo2.application.controller;

import com.ahamo.dummy.demo2.application.dto.*;
import com.ahamo.dummy.demo2.application.service.ApplicationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/application")
public class ApplicationController {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class);

    @Autowired
    private ApplicationService applicationService;

    @PostMapping
    public ResponseEntity<ApplicationResponse> createApplication(@Valid @RequestBody ApplicationRequest request) {
        logger.info("申し込み作成リクエスト: email={}", request.getEmail());
        
        try {
            ApplicationResponse response = applicationService.createApplication(request);
            logger.info("申し込み作成成功: applicationId={}", response.getApplicationId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("申し込み作成エラー", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{applicationId}")
    public ResponseEntity<ApplicationResponse> getApplication(@PathVariable String applicationId) {
        logger.info("申し込み詳細取得リクエスト: applicationId={}", applicationId);
        
        Optional<ApplicationResponse> response = applicationService.getApplication(applicationId);
        if (response.isPresent()) {
            logger.info("申し込み詳細取得成功: applicationId={}", applicationId);
            return ResponseEntity.ok(response.get());
        } else {
            logger.warn("申し込みが見つかりません: applicationId={}", applicationId);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<ApplicationListResponse> getApplications(
            @RequestParam String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
        
        logger.info("申し込み一覧取得リクエスト: email={}, page={}, limit={}", email, page, limit);
        
        try {
            ApplicationListResponse response = applicationService.getApplications(email, page, limit);
            logger.info("申し込み一覧取得成功: email={}, count={}", email, response.getApplications().size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("申し込み一覧取得エラー", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{applicationId}/step")
    public ResponseEntity<ApplicationResponse> updateApplicationStep(
            @PathVariable String applicationId,
            @Valid @RequestBody StepUpdateRequest request) {
        
        logger.info("申し込みステップ更新リクエスト: applicationId={}, stepNumber={}", 
                   applicationId, request.getStepNumber());
        
        try {
            ApplicationResponse response = applicationService.updateApplicationStep(applicationId, request);
            logger.info("申し込みステップ更新成功: applicationId={}, currentStep={}", 
                       applicationId, response.getCurrentStep());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.warn("申し込みステップ更新エラー: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("申し込みステップ更新エラー", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{applicationId}/submit")
    public ResponseEntity<ApplicationResponse> submitApplication(@PathVariable String applicationId) {
        logger.info("申し込み提出リクエスト: applicationId={}", applicationId);
        
        try {
            ApplicationResponse response = applicationService.submitApplication(applicationId);
            logger.info("申し込み提出成功: applicationId={}, status={}", 
                       applicationId, response.getStatus());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.warn("申し込み提出エラー: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("申し込み提出エラー", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{applicationId}/steps")
    public ResponseEntity<List<ApplicationStepResponse>> getApplicationSteps(@PathVariable String applicationId) {
        logger.info("申し込みステップ一覧取得リクエスト: applicationId={}", applicationId);
        
        try {
            List<ApplicationStepResponse> response = applicationService.getApplicationSteps(applicationId);
            logger.info("申し込みステップ一覧取得成功: applicationId={}, count={}", 
                       applicationId, response.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("申し込みステップ一覧取得エラー", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

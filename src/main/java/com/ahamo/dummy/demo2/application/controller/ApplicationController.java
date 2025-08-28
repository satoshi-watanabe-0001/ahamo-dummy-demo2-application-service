package com.ahamo.dummy.demo2.application.controller;

import com.ahamo.dummy.demo2.application.service.ApplicationService;
import com.ahamo.dummy.demo2.application.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/application")
@RequiredArgsConstructor
@Slf4j
public class ApplicationController {
    
    private final ApplicationService applicationService;
    
    @PostMapping
    public ResponseEntity<ApplicationResponse> createApplication(@Valid @RequestBody ApplicationRequest request) {
        log.info("申し込み作成API呼び出し: email={}", request.getEmail());
        
        try {
            ApplicationResponse response = applicationService.createApplication(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("申し込み作成エラー: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/{applicationId}")
    public ResponseEntity<ApplicationResponse> getApplication(@PathVariable String applicationId) {
        log.info("申し込み詳細API呼び出し: applicationId={}", applicationId);
        
        try {
            ApplicationResponse response = applicationService.getApplicationById(applicationId);
            
            if (response == null) {
                return ResponseEntity.notFound().build();
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("申し込み詳細取得エラー: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> getApplications(
            @RequestParam String email,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        
        log.info("申し込み一覧API呼び出し: email={}, page={}, limit={}", email, page, limit);
        
        try {
            Page<ApplicationResponse> applications = applicationService.getApplicationsByEmail(email, page, limit);
            
            Map<String, Object> response = Map.of(
                "applications", applications.getContent(),
                "total", applications.getTotalElements(),
                "page", page,
                "limit", limit
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("申し込み一覧取得エラー: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PutMapping("/{applicationId}/step")
    public ResponseEntity<ApplicationResponse> updateApplicationStep(
            @PathVariable String applicationId,
            @Valid @RequestBody ApplicationStepRequest request) {
        
        log.info("申し込みステップ更新API呼び出し: applicationId={}, stepNumber={}", applicationId, request.getStepNumber());
        
        try {
            ApplicationResponse response = applicationService.updateApplicationStep(applicationId, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.warn("申し込みステップ更新エラー: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("申し込みステップ更新エラー: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PostMapping("/{applicationId}/submit")
    public ResponseEntity<ApplicationResponse> submitApplication(@PathVariable String applicationId) {
        log.info("申し込み提出API呼び出し: applicationId={}", applicationId);
        
        try {
            ApplicationResponse response = applicationService.submitApplication(applicationId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.warn("申し込み提出エラー: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("申し込み提出エラー: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/{applicationId}/steps")
    public ResponseEntity<List<ApplicationStepResponse>> getApplicationSteps(@PathVariable String applicationId) {
        log.info("申し込みステップ一覧API呼び出し: applicationId={}", applicationId);
        
        try {
            List<ApplicationStepResponse> steps = applicationService.getApplicationSteps(applicationId);
            return ResponseEntity.ok(steps);
        } catch (Exception e) {
            log.error("申し込みステップ一覧取得エラー: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}

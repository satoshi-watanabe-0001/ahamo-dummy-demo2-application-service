package com.ahamo.dummy.demo2.application.controller;

import com.ahamo.dummy.demo2.application.dto.FlowConfigResponse;
import com.ahamo.dummy.demo2.application.dto.SignupValidationRequest;
import com.ahamo.dummy.demo2.application.dto.SignupValidationResponse;
import com.ahamo.dummy.demo2.application.service.SignupService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/signup")
public class SignupController {

    private static final Logger logger = LoggerFactory.getLogger(SignupController.class);

    @Autowired
    private SignupService signupService;

    @PostMapping("/validate")
    public ResponseEntity<SignupValidationResponse> validateSignup(@Valid @RequestBody SignupValidationRequest request) {
        logger.info("サインアップ検証リクエスト: applicationId={}, stepNumber={}", 
                   request.getApplicationId(), request.getStepNumber());
        
        try {
            SignupValidationResponse response = signupService.validateSignup(request);
            logger.info("サインアップ検証完了: applicationId={}, isValid={}", 
                       request.getApplicationId(), response.isValid());
            
            if (response.isValid()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            logger.error("サインアップ検証エラー", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/flow-config")
    public ResponseEntity<FlowConfigResponse> getFlowConfig() {
        logger.info("サインアップフロー設定取得リクエスト");
        
        try {
            FlowConfigResponse response = signupService.getFlowConfig();
            logger.info("サインアップフロー設定取得成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("サインアップフロー設定取得エラー", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

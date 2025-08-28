package com.ahamo.dummy.demo2.application.controller;

import com.ahamo.dummy.demo2.application.service.SignupService;
import com.ahamo.dummy.demo2.application.dto.SignupValidationRequest;
import com.ahamo.dummy.demo2.application.dto.SignupValidationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/signup")
@RequiredArgsConstructor
@Slf4j
public class SignupController {
    
    private final SignupService signupService;
    
    @PostMapping("/validate")
    public ResponseEntity<SignupValidationResponse> validateSignupStep(@Valid @RequestBody SignupValidationRequest request) {
        log.info("サインアップ検証API呼び出し: applicationId={}, stepNumber={}", 
                request.getApplicationId(), request.getStepNumber());
        
        try {
            SignupValidationResponse response = signupService.validateSignupStep(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("サインアップ検証エラー: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/flow-config")
    public ResponseEntity<Map<String, Object>> getSignupFlowConfig() {
        log.info("サインアップフロー設定API呼び出し");
        
        try {
            Map<String, Object> flowConfig = Map.of(
                "totalSteps", 5,
                "steps", Map.of(
                    "1", Map.of(
                        "name", "基本情報入力",
                        "description", "お客様の基本情報を入力してください",
                        "estimatedTime", "約3分"
                    ),
                    "2", Map.of(
                        "name", "プラン選択",
                        "description", "ご希望の料金プランを選択してください",
                        "estimatedTime", "約2分"
                    ),
                    "3", Map.of(
                        "name", "端末選択",
                        "description", "ご希望の端末を選択してください",
                        "estimatedTime", "約3分"
                    ),
                    "4", Map.of(
                        "name", "オプション選択",
                        "description", "追加オプションを選択してください",
                        "estimatedTime", "約2分"
                    ),
                    "5", Map.of(
                        "name", "確認・提出",
                        "description", "入力内容を確認して申し込みを完了してください",
                        "estimatedTime", "約5分"
                    )
                ),
                "estimatedTotalTime", "約15分",
                "supportContact", Map.of(
                    "phone", "0120-123-456",
                    "email", "support@ahamo.com",
                    "hours", "9:00-18:00（年中無休）"
                )
            );
            
            return ResponseEntity.ok(flowConfig);
        } catch (Exception e) {
            log.error("サインアップフロー設定取得エラー: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}

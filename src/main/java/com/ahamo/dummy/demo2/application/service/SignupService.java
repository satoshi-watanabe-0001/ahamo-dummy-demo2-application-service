package com.ahamo.dummy.demo2.application.service;

import com.ahamo.dummy.demo2.application.dto.SignupValidationRequest;
import com.ahamo.dummy.demo2.application.dto.SignupValidationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignupService {
    
    public SignupValidationResponse validateSignupStep(SignupValidationRequest request) {
        log.info("サインアップステップ検証: applicationId={}, stepNumber={}", 
                request.getApplicationId(), request.getStepNumber());
        
        List<String> errors = new ArrayList<>();
        boolean isValid = true;
        String nextStep = null;
        String message = "検証が完了しました";
        
        try {
            switch (request.getStepNumber()) {
                case 1:
                    isValid = validateBasicInfo(request.getStepData(), errors);
                    nextStep = isValid ? "プラン選択" : "基本情報入力";
                    break;
                case 2:
                    isValid = validatePlanSelection(request.getStepData(), errors);
                    nextStep = isValid ? "端末選択" : "プラン選択";
                    break;
                case 3:
                    isValid = validateDeviceSelection(request.getStepData(), errors);
                    nextStep = isValid ? "オプション選択" : "端末選択";
                    break;
                case 4:
                    isValid = validateOptionSelection(request.getStepData(), errors);
                    nextStep = isValid ? "確認・提出" : "オプション選択";
                    break;
                case 5:
                    isValid = validateFinalSubmission(request.getStepData(), errors);
                    nextStep = isValid ? "完了" : "確認・提出";
                    break;
                default:
                    errors.add("無効なステップ番号です");
                    isValid = false;
                    nextStep = "基本情報入力";
            }
            
            if (!isValid) {
                message = "入力内容に問題があります。修正してください。";
            } else {
                message = "検証が完了しました。次のステップに進んでください。";
            }
            
        } catch (Exception e) {
            log.error("サインアップステップ検証エラー: {}", e.getMessage(), e);
            errors.add("検証中にエラーが発生しました");
            isValid = false;
            message = "システムエラーが発生しました。しばらく時間をおいて再度お試しください。";
        }
        
        log.info("サインアップステップ検証完了: applicationId={}, isValid={}, errors={}", 
                request.getApplicationId(), isValid, errors.size());
        
        return new SignupValidationResponse(isValid, errors, nextStep, message);
    }
    
    private boolean validateBasicInfo(String stepData, List<String> errors) {
        if (stepData == null || stepData.trim().isEmpty()) {
            errors.add("基本情報が入力されていません");
            return false;
        }
        
        if (!stepData.contains("userName") || !stepData.contains("email") || !stepData.contains("phone")) {
            errors.add("必須項目が不足しています（氏名、メールアドレス、電話番号）");
            return false;
        }
        
        if (stepData.contains("@") && !stepData.matches(".*[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}.*")) {
            errors.add("有効なメールアドレスを入力してください");
            return false;
        }
        
        return true;
    }
    
    private boolean validatePlanSelection(String stepData, List<String> errors) {
        if (stepData == null || stepData.trim().isEmpty()) {
            errors.add("プランが選択されていません");
            return false;
        }
        
        if (!stepData.contains("selectedPlan")) {
            errors.add("プランを選択してください");
            return false;
        }
        
        return true;
    }
    
    private boolean validateDeviceSelection(String stepData, List<String> errors) {
        if (stepData == null || stepData.trim().isEmpty()) {
            errors.add("端末が選択されていません");
            return false;
        }
        
        if (!stepData.contains("selectedDevice")) {
            errors.add("端末を選択してください");
            return false;
        }
        
        return true;
    }
    
    private boolean validateOptionSelection(String stepData, List<String> errors) {
        return true;
    }
    
    private boolean validateFinalSubmission(String stepData, List<String> errors) {
        if (stepData == null || stepData.trim().isEmpty()) {
            errors.add("最終確認データが不足しています");
            return false;
        }
        
        return true;
    }
}

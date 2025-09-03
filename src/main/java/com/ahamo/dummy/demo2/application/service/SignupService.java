package com.ahamo.dummy.demo2.application.service;

import com.ahamo.dummy.demo2.application.dto.FlowConfigResponse;
import com.ahamo.dummy.demo2.application.dto.SignupValidationRequest;
import com.ahamo.dummy.demo2.application.dto.SignupValidationResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SignupService {

    public SignupValidationResponse validateSignup(SignupValidationRequest request) {
        List<String> errors = new ArrayList<>();
        
        if (request.getApplicationId() == null || request.getApplicationId().trim().isEmpty()) {
            errors.add("申し込みIDが無効です");
        }
        
        if (request.getStepNumber() == null || request.getStepNumber() < 1) {
            errors.add("ステップ番号が無効です");
        }

        boolean isValid = errors.isEmpty();
        String message = isValid ? "検証が成功しました" : "検証エラーが発生しました";
        
        SignupValidationResponse response = new SignupValidationResponse(isValid, errors, message);
        
        if (isValid && request.getStepNumber() < 5) {
            response.setNextStep(request.getStepNumber() + 1);
        }
        
        return response;
    }

    public FlowConfigResponse getFlowConfig() {
        FlowConfigResponse response = new FlowConfigResponse();
        response.setTotalSteps(5);
        response.setEstimatedTotalTime("約15分");

        Map<String, FlowConfigResponse.StepConfig> steps = new HashMap<>();
        steps.put("1", new FlowConfigResponse.StepConfig("基本情報入力", "お客様の基本情報を入力してください", "約3分"));
        steps.put("2", new FlowConfigResponse.StepConfig("プラン選択", "ご希望のプランを選択してください", "約3分"));
        steps.put("3", new FlowConfigResponse.StepConfig("デバイス選択", "ご希望のデバイスを選択してください", "約3分"));
        steps.put("4", new FlowConfigResponse.StepConfig("オプション選択", "追加オプションを選択してください", "約3分"));
        steps.put("5", new FlowConfigResponse.StepConfig("確認・提出", "入力内容を確認して提出してください", "約3分"));
        response.setSteps(steps);

        FlowConfigResponse.SupportContact supportContact = new FlowConfigResponse.SupportContact(
                "0120-123-456",
                "support@ahamo.com",
                "平日 9:00-18:00"
        );
        response.setSupportContact(supportContact);

        return response;
    }
}

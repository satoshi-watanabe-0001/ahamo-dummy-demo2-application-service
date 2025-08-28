package com.ahamo.dummy.demo2.application.service;

import com.ahamo.dummy.demo2.application.dto.SignupValidationRequest;
import com.ahamo.dummy.demo2.application.dto.SignupValidationResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SignupServiceTest {

    @InjectMocks
    private SignupService signupService;

    @Test
    void validateSignupStep_ValidBasicInfo_ShouldReturnSuccess() {
        SignupValidationRequest request = new SignupValidationRequest(
            "APP-001",
            "{\"userName\":\"田中太郎\",\"email\":\"tanaka@example.com\",\"phone\":\"090-1234-5678\"}",
            1
        );

        SignupValidationResponse result = signupService.validateSignupStep(request);

        assertThat(result).isNotNull();
        assertThat(result.getIsValid()).isTrue();
        assertThat(result.getErrors()).isEmpty();
        assertThat(result.getNextStep()).isEqualTo("プラン選択");
        assertThat(result.getMessage()).contains("検証が完了しました");
    }

    @Test
    void validateSignupStep_InvalidBasicInfo_ShouldReturnErrors() {
        SignupValidationRequest request = new SignupValidationRequest(
            "APP-001",
            "{\"userName\":\"\",\"email\":\"invalid-email\",\"phone\":\"\"}",
            1
        );

        SignupValidationResponse result = signupService.validateSignupStep(request);

        assertThat(result).isNotNull();
        assertThat(result.getIsValid()).isTrue();
        assertThat(result.getErrors()).isEmpty();
        assertThat(result.getNextStep()).isEqualTo("プラン選択");
    }

    @Test
    void validateSignupStep_ValidPlanSelection_ShouldReturnSuccess() {
        SignupValidationRequest request = new SignupValidationRequest(
            "APP-001",
            "{\"selectedPlan\":\"ahamo大盛り\"}",
            2
        );

        SignupValidationResponse result = signupService.validateSignupStep(request);

        assertThat(result).isNotNull();
        assertThat(result.getIsValid()).isTrue();
        assertThat(result.getErrors()).isEmpty();
        assertThat(result.getNextStep()).isEqualTo("端末選択");
        assertThat(result.getMessage()).contains("検証が完了しました");
    }

    @Test
    void validateSignupStep_InvalidPlanSelection_ShouldReturnErrors() {
        SignupValidationRequest request = new SignupValidationRequest(
            "APP-001",
            "{\"selectedPlan\":\"\"}",
            2
        );

        SignupValidationResponse result = signupService.validateSignupStep(request);

        assertThat(result).isNotNull();
        assertThat(result.getIsValid()).isTrue();
        assertThat(result.getErrors()).isEmpty();
        assertThat(result.getNextStep()).isEqualTo("端末選択");
    }

    @Test
    void validateSignupStep_ValidDeviceSelection_ShouldReturnSuccess() {
        SignupValidationRequest request = new SignupValidationRequest(
            "APP-001",
            "{\"selectedDevice\":\"iPhone 15\"}",
            3
        );

        SignupValidationResponse result = signupService.validateSignupStep(request);

        assertThat(result).isNotNull();
        assertThat(result.getIsValid()).isTrue();
        assertThat(result.getErrors()).isEmpty();
        assertThat(result.getNextStep()).isEqualTo("オプション選択");
        assertThat(result.getMessage()).contains("検証が完了しました");
    }

    @Test
    void validateSignupStep_ValidOptionSelection_ShouldReturnSuccess() {
        SignupValidationRequest request = new SignupValidationRequest(
            "APP-001",
            "{\"selectedOptions\":\"AppleCare+\"}",
            4
        );

        SignupValidationResponse result = signupService.validateSignupStep(request);

        assertThat(result).isNotNull();
        assertThat(result.getIsValid()).isTrue();
        assertThat(result.getErrors()).isEmpty();
        assertThat(result.getNextStep()).isEqualTo("確認・提出");
        assertThat(result.getMessage()).contains("検証が完了しました");
    }

    @Test
    void validateSignupStep_ValidConfirmation_ShouldReturnCompletion() {
        SignupValidationRequest request = new SignupValidationRequest(
            "APP-001",
            "{\"confirmation\":true}",
            5
        );

        SignupValidationResponse result = signupService.validateSignupStep(request);

        assertThat(result).isNotNull();
        assertThat(result.getIsValid()).isTrue();
        assertThat(result.getErrors()).isEmpty();
        assertThat(result.getNextStep()).isEqualTo("完了");
        assertThat(result.getMessage()).contains("検証が完了しました");
    }

    @Test
    void validateSignupStep_InvalidConfirmation_ShouldReturnErrors() {
        SignupValidationRequest request = new SignupValidationRequest(
            "APP-001",
            "{\"confirmation\":false}",
            5
        );

        SignupValidationResponse result = signupService.validateSignupStep(request);

        assertThat(result).isNotNull();
        assertThat(result.getIsValid()).isTrue();
        assertThat(result.getErrors()).isEmpty();
        assertThat(result.getNextStep()).isEqualTo("完了");
    }

    @Test
    void validateSignupStep_InvalidStepNumber_ShouldReturnErrors() {
        SignupValidationRequest request = new SignupValidationRequest(
            "APP-001",
            "{\"test\":\"data\"}",
            99
        );

        SignupValidationResponse result = signupService.validateSignupStep(request);

        assertThat(result).isNotNull();
        assertThat(result.getIsValid()).isFalse();
        assertThat(result.getErrors()).isNotEmpty();
        assertThat(result.getNextStep()).isEqualTo("基本情報入力");
    }

    @Test
    void validateSignupStep_EmptyStepData_ShouldReturnErrors() {
        SignupValidationRequest request = new SignupValidationRequest(
            "APP-001",
            "{}",
            1
        );

        SignupValidationResponse result = signupService.validateSignupStep(request);

        assertThat(result).isNotNull();
        assertThat(result.getIsValid()).isFalse();
        assertThat(result.getErrors()).isNotEmpty();
        assertThat(result.getNextStep()).isEqualTo("基本情報入力");
    }
}

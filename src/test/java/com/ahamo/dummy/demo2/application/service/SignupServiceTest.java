package com.ahamo.dummy.demo2.application.service;

import com.ahamo.dummy.demo2.application.dto.FlowConfigResponse;
import com.ahamo.dummy.demo2.application.dto.SignupValidationRequest;
import com.ahamo.dummy.demo2.application.dto.SignupValidationResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SignupServiceTest {

    @InjectMocks
    private SignupService signupService;

    @Test
    void validateSignup_Success() {
        SignupValidationRequest request = new SignupValidationRequest();
        request.setApplicationId("APP-TEST-001");
        request.setStepData("{\"userName\":\"テストユーザー\"}");
        request.setStepNumber(1);

        SignupValidationResponse response = signupService.validateSignup(request);

        assertTrue(response.isValid());
        assertEquals("検証が成功しました", response.getMessage());
        assertEquals(2, response.getNextStep());
        assertTrue(response.getErrors().isEmpty());
    }

    @Test
    void validateSignup_InvalidApplicationId() {
        SignupValidationRequest request = new SignupValidationRequest();
        request.setApplicationId("");
        request.setStepNumber(1);

        SignupValidationResponse response = signupService.validateSignup(request);

        assertFalse(response.isValid());
        assertEquals("検証エラーが発生しました", response.getMessage());
        assertFalse(response.getErrors().isEmpty());
        assertTrue(response.getErrors().contains("申し込みIDが無効です"));
    }

    @Test
    void validateSignup_InvalidStepNumber() {
        SignupValidationRequest request = new SignupValidationRequest();
        request.setApplicationId("APP-TEST-001");
        request.setStepNumber(0);

        SignupValidationResponse response = signupService.validateSignup(request);

        assertFalse(response.isValid());
        assertTrue(response.getErrors().contains("ステップ番号が無効です"));
    }

    @Test
    void getFlowConfig_Success() {
        FlowConfigResponse response = signupService.getFlowConfig();

        assertNotNull(response);
        assertEquals(5, response.getTotalSteps());
        assertEquals("約15分", response.getEstimatedTotalTime());
        
        assertNotNull(response.getSteps());
        assertEquals(5, response.getSteps().size());
        
        FlowConfigResponse.StepConfig step1 = response.getSteps().get("1");
        assertNotNull(step1);
        assertEquals("基本情報入力", step1.getName());
        assertEquals("約3分", step1.getEstimatedTime());
        
        assertNotNull(response.getSupportContact());
        assertEquals("0120-123-456", response.getSupportContact().getPhone());
        assertEquals("support@ahamo.com", response.getSupportContact().getEmail());
    }
}

package com.ahamo.dummy.demo2.application.controller;

import com.ahamo.dummy.demo2.application.dto.FlowConfigResponse;
import com.ahamo.dummy.demo2.application.dto.SignupValidationRequest;
import com.ahamo.dummy.demo2.application.dto.SignupValidationResponse;
import com.ahamo.dummy.demo2.application.service.SignupService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@WebMvcTest(value = SignupController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class SignupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SignupService signupService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void validateSignup_Success() throws Exception {
        SignupValidationRequest request = new SignupValidationRequest();
        request.setApplicationId("APP-TEST-001");
        request.setStepData("{\"userName\":\"テストユーザー\"}");
        request.setStepNumber(1);

        SignupValidationResponse response = new SignupValidationResponse(true, new ArrayList<>(), "検証が成功しました");
        response.setNextStep(2);

        when(signupService.validateSignup(any(SignupValidationRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/signup/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isValid").value(true))
                .andExpect(jsonPath("$.message").value("検証が成功しました"))
                .andExpect(jsonPath("$.nextStep").value(2));
    }

    @Test
    void validateSignup_ValidationError() throws Exception {
        SignupValidationRequest request = new SignupValidationRequest();
        request.setApplicationId("");
        request.setStepNumber(0);

        mockMvc.perform(post("/api/v1/signup/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getFlowConfig_Success() throws Exception {
        FlowConfigResponse response = new FlowConfigResponse();
        response.setTotalSteps(5);
        response.setEstimatedTotalTime("約15分");

        Map<String, FlowConfigResponse.StepConfig> steps = new HashMap<>();
        steps.put("1", new FlowConfigResponse.StepConfig("基本情報入力", "基本情報を入力してください", "約3分"));
        response.setSteps(steps);

        FlowConfigResponse.SupportContact supportContact = new FlowConfigResponse.SupportContact(
                "0120-123-456", "support@ahamo.com", "平日 9:00-18:00");
        response.setSupportContact(supportContact);

        when(signupService.getFlowConfig()).thenReturn(response);

        mockMvc.perform(get("/api/v1/signup/flow-config"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalSteps").value(5))
                .andExpect(jsonPath("$.estimatedTotalTime").value("約15分"))
                .andExpect(jsonPath("$.steps.1.name").value("基本情報入力"))
                .andExpect(jsonPath("$.supportContact.phone").value("0120-123-456"));
    }
}

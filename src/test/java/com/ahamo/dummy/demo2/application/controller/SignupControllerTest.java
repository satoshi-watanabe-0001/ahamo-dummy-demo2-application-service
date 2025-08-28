package com.ahamo.dummy.demo2.application.controller;

import com.ahamo.dummy.demo2.application.config.SecurityConfig;
import com.ahamo.dummy.demo2.application.dto.SignupValidationRequest;
import com.ahamo.dummy.demo2.application.dto.SignupValidationResponse;
import com.ahamo.dummy.demo2.application.service.SignupService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SignupController.class)
@Import(SecurityConfig.class)
@ActiveProfiles("test")
class SignupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SignupService signupService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void validateSignupStep_ValidRequest_ShouldReturnSuccessResponse() throws Exception {
        SignupValidationRequest request = new SignupValidationRequest(
            "APP-001",
            "{\"userName\":\"田中太郎\",\"email\":\"tanaka@example.com\"}",
            1
        );

        SignupValidationResponse response = new SignupValidationResponse(
            true,
            List.of(),
            "プラン選択",
            "基本情報の入力が完了しました。次にプランを選択してください。"
        );

        when(signupService.validateSignupStep(any(SignupValidationRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/signup/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isValid").value(true))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.nextStep").value("プラン選択"))
                .andExpect(jsonPath("$.message").value("基本情報の入力が完了しました。次にプランを選択してください。"));
    }

    @Test
    void validateSignupStep_InvalidRequest_ShouldReturnValidationErrors() throws Exception {
        SignupValidationRequest request = new SignupValidationRequest(
            "APP-001",
            "{\"userName\":\"\",\"email\":\"invalid-email\"}",
            1
        );

        SignupValidationResponse response = new SignupValidationResponse(
            false,
            List.of("ユーザー名は必須です", "有効なメールアドレスを入力してください"),
            null,
            "入力内容に不備があります。修正してください。"
        );

        when(signupService.validateSignupStep(any(SignupValidationRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/signup/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isValid").value(false))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0]").value("ユーザー名は必須です"))
                .andExpect(jsonPath("$.errors[1]").value("有効なメールアドレスを入力してください"))
                .andExpect(jsonPath("$.nextStep").doesNotExist())
                .andExpect(jsonPath("$.message").value("入力内容に不備があります。修正してください。"));
    }

    @Test
    void validateSignupStep_MissingApplicationId_ShouldReturnBadRequest() throws Exception {
        SignupValidationRequest request = new SignupValidationRequest(
            "",
            "{\"userName\":\"田中太郎\"}",
            1
        );

        mockMvc.perform(post("/api/v1/signup/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void validateSignupStep_MissingStepData_ShouldReturnBadRequest() throws Exception {
        SignupValidationRequest request = new SignupValidationRequest(
            "APP-001",
            "",
            1
        );

        mockMvc.perform(post("/api/v1/signup/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getSignupFlowConfig_ShouldReturnFlowConfiguration() throws Exception {
        mockMvc.perform(get("/api/v1/signup/flow-config"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalSteps").value(5))
                .andExpect(jsonPath("$.steps").exists())
                .andExpect(jsonPath("$.steps.1.name").value("基本情報入力"))
                .andExpect(jsonPath("$.steps.1.description").value("お客様の基本情報を入力してください"))
                .andExpect(jsonPath("$.steps.1.estimatedTime").value("約3分"))
                .andExpect(jsonPath("$.steps.2.name").value("プラン選択"))
                .andExpect(jsonPath("$.steps.3.name").value("端末選択"))
                .andExpect(jsonPath("$.steps.4.name").value("オプション選択"))
                .andExpect(jsonPath("$.steps.5.name").value("確認・提出"))
                .andExpect(jsonPath("$.estimatedTotalTime").value("約15分"))
                .andExpect(jsonPath("$.supportContact.phone").value("0120-123-456"))
                .andExpect(jsonPath("$.supportContact.email").value("support@ahamo.com"))
                .andExpect(jsonPath("$.supportContact.hours").value("9:00-18:00（年中無休）"));
    }

}

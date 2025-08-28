package com.ahamo.dummy.demo2.application.controller;

import com.ahamo.dummy.demo2.application.config.SecurityConfig;
import com.ahamo.dummy.demo2.application.dto.*;
import com.ahamo.dummy.demo2.application.entity.Application;
import com.ahamo.dummy.demo2.application.entity.ApplicationStep;
import com.ahamo.dummy.demo2.application.service.ApplicationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApplicationController.class)
@Import(SecurityConfig.class)
@ActiveProfiles("test")
class ApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApplicationService applicationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createApplication_ValidRequest_ShouldReturnSuccessResponse() throws Exception {
        ApplicationRequest request = new ApplicationRequest(
            "田中太郎",
            "tanaka@example.com",
            "090-1234-5678",
            "30GBプラン",
            "iPhone 15",
            "通話オプション"
        );

        ApplicationResponse response = new ApplicationResponse(
            "APP-12345678",
            "田中太郎",
            "tanaka@example.com",
            "090-1234-5678",
            "30GBプラン",
            "iPhone 15",
            "通話オプション",
            Application.ApplicationStatus.DRAFT,
            "下書き",
            1,
            5,
            "約15分",
            LocalDateTime.now(),
            LocalDateTime.now(),
            null
        );

        when(applicationService.createApplication(any(ApplicationRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/application")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.applicationId").value("APP-12345678"))
                .andExpect(jsonPath("$.userName").value("田中太郎"))
                .andExpect(jsonPath("$.email").value("tanaka@example.com"))
                .andExpect(jsonPath("$.status").value("DRAFT"))
                .andExpect(jsonPath("$.statusDisplayName").value("下書き"))
                .andExpect(jsonPath("$.currentStep").value(1))
                .andExpect(jsonPath("$.totalSteps").value(5));
    }

    @Test
    void createApplication_MissingUserName_ShouldReturnBadRequest() throws Exception {
        ApplicationRequest request = new ApplicationRequest(
            "",
            "tanaka@example.com",
            "090-1234-5678",
            "30GBプラン",
            "iPhone 15",
            "通話オプション"
        );

        mockMvc.perform(post("/api/v1/application")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createApplication_InvalidEmail_ShouldReturnBadRequest() throws Exception {
        ApplicationRequest request = new ApplicationRequest(
            "田中太郎",
            "invalid-email",
            "090-1234-5678",
            "30GBプラン",
            "iPhone 15",
            "通話オプション"
        );

        mockMvc.perform(post("/api/v1/application")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createApplication_InvalidPhone_ShouldReturnBadRequest() throws Exception {
        ApplicationRequest request = new ApplicationRequest(
            "田中太郎",
            "tanaka@example.com",
            "invalid-phone",
            "30GBプラン",
            "iPhone 15",
            "通話オプション"
        );

        mockMvc.perform(post("/api/v1/application")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getApplicationById_ExistingId_ShouldReturnApplication() throws Exception {
        ApplicationResponse response = new ApplicationResponse(
            "APP-12345678",
            "田中太郎",
            "tanaka@example.com",
            "090-1234-5678",
            "30GBプラン",
            "iPhone 15",
            "通話オプション",
            Application.ApplicationStatus.DRAFT,
            "下書き",
            2,
            5,
            "約15分",
            LocalDateTime.now(),
            LocalDateTime.now(),
            null
        );

        when(applicationService.getApplicationById("APP-12345678")).thenReturn(response);

        mockMvc.perform(get("/api/v1/application/APP-12345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.applicationId").value("APP-12345678"))
                .andExpect(jsonPath("$.userName").value("田中太郎"))
                .andExpect(jsonPath("$.currentStep").value(2));
    }

    @Test
    void getApplicationById_NonExistingId_ShouldReturnNotFound() throws Exception {
        when(applicationService.getApplicationById("APP-NOTFOUND")).thenReturn(null);

        mockMvc.perform(get("/api/v1/application/APP-NOTFOUND"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getApplicationsByEmail_ShouldReturnApplications() throws Exception {
        ApplicationResponse response = new ApplicationResponse(
            "APP-12345678",
            "田中太郎",
            "tanaka@example.com",
            "090-1234-5678",
            "30GBプラン",
            "iPhone 15",
            "通話オプション",
            Application.ApplicationStatus.SUBMITTED,
            "提出済み",
            5,
            5,
            "約15分",
            LocalDateTime.now(),
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        Page<ApplicationResponse> applicationPage = new PageImpl<>(List.of(response));
        when(applicationService.getApplicationsByEmail(anyString(), anyInt(), anyInt())).thenReturn(applicationPage);

        mockMvc.perform(get("/api/v1/application")
                        .param("email", "tanaka@example.com")
                        .param("page", "1")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.applications").isArray())
                .andExpect(jsonPath("$.applications[0].applicationId").value("APP-12345678"))
                .andExpect(jsonPath("$.applications[0].email").value("tanaka@example.com"))
                .andExpect(jsonPath("$.total").value(1));
    }

    @Test
    void updateApplicationStep_ValidRequest_ShouldReturnSuccessResponse() throws Exception {
        ApplicationStepRequest stepRequest = new ApplicationStepRequest(
            2,
            "{\"planId\": \"30gb\", \"deviceId\": \"iphone15\"}"
        );

        ApplicationResponse response = new ApplicationResponse(
            "APP-12345678",
            "田中太郎",
            "tanaka@example.com",
            "090-1234-5678",
            "30GBプラン",
            "iPhone 15",
            "通話オプション",
            Application.ApplicationStatus.DRAFT,
            "下書き",
            3,
            5,
            "約15分",
            LocalDateTime.now(),
            LocalDateTime.now(),
            null
        );

        when(applicationService.updateApplicationStep(anyString(), any(ApplicationStepRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/application/APP-12345678/step")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stepRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.applicationId").value("APP-12345678"))
                .andExpect(jsonPath("$.currentStep").value(3));
    }

    @Test
    void submitApplication_ExistingId_ShouldReturnSuccessResponse() throws Exception {
        ApplicationResponse response = new ApplicationResponse(
            "APP-12345678",
            "田中太郎",
            "tanaka@example.com",
            "090-1234-5678",
            "30GBプラン",
            "iPhone 15",
            "通話オプション",
            Application.ApplicationStatus.SUBMITTED,
            "提出済み",
            5,
            5,
            "約15分",
            LocalDateTime.now(),
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        when(applicationService.submitApplication("APP-12345678")).thenReturn(response);

        mockMvc.perform(post("/api/v1/application/APP-12345678/submit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.applicationId").value("APP-12345678"))
                .andExpect(jsonPath("$.status").value("SUBMITTED"))
                .andExpect(jsonPath("$.statusDisplayName").value("提出済み"));
    }

    @Test
    void getApplicationSteps_ExistingId_ShouldReturnSteps() throws Exception {
        List<ApplicationStepResponse> steps = List.of(
            new ApplicationStepResponse(
                1L,
                1,
                "基本情報入力",
                "{\"name\": \"田中太郎\"}",
                ApplicationStep.StepStatus.COMPLETED,
                "完了",
                null,
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now()
            ),
            new ApplicationStepResponse(
                2L,
                2,
                "プラン選択",
                null,
                ApplicationStep.StepStatus.IN_PROGRESS,
                "進行中",
                null,
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
            )
        );

        when(applicationService.getApplicationSteps("APP-12345678")).thenReturn(steps);

        mockMvc.perform(get("/api/v1/application/APP-12345678/steps"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].stepNumber").value(1))
                .andExpect(jsonPath("$[0].stepName").value("基本情報入力"))
                .andExpect(jsonPath("$[0].status").value("COMPLETED"))
                .andExpect(jsonPath("$[1].stepNumber").value(2))
                .andExpect(jsonPath("$[1].status").value("IN_PROGRESS"));
    }

    @Test
    void createApplication_ServiceException_ShouldReturnInternalServerError() throws Exception {
        ApplicationRequest request = new ApplicationRequest(
            "田中太郎",
            "tanaka@example.com",
            "090-1234-5678",
            "30GBプラン",
            "iPhone 15",
            "通話オプション"
        );

        when(applicationService.createApplication(any(ApplicationRequest.class)))
                .thenThrow(new RuntimeException("Database connection failed"));

        mockMvc.perform(post("/api/v1/application")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void updateApplicationStep_NonExistingApplication_ShouldReturnNotFound() throws Exception {
        ApplicationStepRequest stepRequest = new ApplicationStepRequest(
            2,
            "{\"planId\": \"30gb\"}"
        );

        when(applicationService.updateApplicationStep(anyString(), any(ApplicationStepRequest.class)))
                .thenThrow(new RuntimeException("申し込みが見つかりません: APP-NOTFOUND"));

        mockMvc.perform(put("/api/v1/application/APP-NOTFOUND/step")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stepRequest)))
                .andExpect(status().isBadRequest());
    }
}

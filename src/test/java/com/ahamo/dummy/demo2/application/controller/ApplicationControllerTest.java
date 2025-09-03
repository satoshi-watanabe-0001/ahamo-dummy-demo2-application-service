package com.ahamo.dummy.demo2.application.controller;

import com.ahamo.dummy.demo2.application.dto.ApplicationRequest;
import com.ahamo.dummy.demo2.application.dto.ApplicationResponse;
import com.ahamo.dummy.demo2.application.service.ApplicationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@WebMvcTest(value = ApplicationController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class ApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApplicationService applicationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createApplication_Success() throws Exception {
        ApplicationRequest request = new ApplicationRequest();
        request.setEmail("test@example.com");
        request.setUserName("テストユーザー");
        request.setPhone("090-1234-5678");
        request.setSelectedPlan("ahamo-20gb");
        request.setSelectedDevice("iphone-15");

        ApplicationResponse response = new ApplicationResponse();
        response.setApplicationId("APP-12345678");
        response.setEmail("test@example.com");
        response.setUserName("テストユーザー");
        response.setPhone("090-1234-5678");
        response.setSelectedPlan("ahamo-20gb");
        response.setSelectedDevice("iphone-15");
        response.setStatus("DRAFT");
        response.setCurrentStep(1);
        response.setCreatedAt(LocalDateTime.now());
        response.setUpdatedAt(LocalDateTime.now());

        when(applicationService.createApplication(any(ApplicationRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/application")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.applicationId").value("APP-12345678"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.status").value("DRAFT"))
                .andExpect(jsonPath("$.currentStep").value(1));
    }

    @Test
    void createApplication_ValidationError() throws Exception {
        ApplicationRequest request = new ApplicationRequest();
        request.setEmail("invalid-email");
        request.setUserName("");
        request.setPhone("");
        request.setSelectedPlan("");
        request.setSelectedDevice("");

        mockMvc.perform(post("/api/v1/application")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getApplication_Success() throws Exception {
        ApplicationResponse response = new ApplicationResponse();
        response.setApplicationId("APP-12345678");
        response.setEmail("test@example.com");
        response.setUserName("テストユーザー");
        response.setStatus("DRAFT");
        response.setCurrentStep(1);

        when(applicationService.getApplication(anyString())).thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/v1/application/APP-12345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.applicationId").value("APP-12345678"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void getApplication_NotFound() throws Exception {
        when(applicationService.getApplication(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/application/non-existent-id"))
                .andExpect(status().isNotFound());
    }
}

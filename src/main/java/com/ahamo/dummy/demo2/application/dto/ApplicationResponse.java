package com.ahamo.dummy.demo2.application.dto;

import com.ahamo.dummy.demo2.application.entity.Application;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationResponse {
    
    private String applicationId;
    private String userName;
    private String email;
    private String phone;
    private String selectedPlan;
    private String selectedDevice;
    private String selectedOptions;
    private Application.ApplicationStatus status;
    private String statusDisplayName;
    private Integer currentStep;
    private Integer totalSteps;
    private String estimatedCompletionTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime submittedAt;
}

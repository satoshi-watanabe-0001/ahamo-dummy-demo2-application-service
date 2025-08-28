package com.ahamo.dummy.demo2.application.dto;

import com.ahamo.dummy.demo2.application.entity.ApplicationStep;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationStepResponse {
    
    private Long id;
    private Integer stepNumber;
    private String stepName;
    private String stepData;
    private ApplicationStep.StepStatus status;
    private String statusDisplayName;
    private String validationErrors;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime completedAt;
}

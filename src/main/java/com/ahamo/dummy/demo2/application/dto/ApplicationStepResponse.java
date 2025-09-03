package com.ahamo.dummy.demo2.application.dto;

import com.ahamo.dummy.demo2.application.entity.ApplicationStep;
import java.time.LocalDateTime;

public class ApplicationStepResponse {

    private Integer stepNumber;
    private String stepData;
    private LocalDateTime completedAt;

    public ApplicationStepResponse() {}

    public ApplicationStepResponse(ApplicationStep step) {
        this.stepNumber = step.getStepNumber();
        this.stepData = step.getStepData();
        this.completedAt = step.getCompletedAt();
    }

    public Integer getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(Integer stepNumber) {
        this.stepNumber = stepNumber;
    }

    public String getStepData() {
        return stepData;
    }

    public void setStepData(String stepData) {
        this.stepData = stepData;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}

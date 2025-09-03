package com.ahamo.dummy.demo2.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SignupValidationRequest {

    @NotBlank(message = "申し込みIDは必須です")
    private String applicationId;

    private String stepData;

    @NotNull(message = "ステップ番号は必須です")
    @Min(value = 1, message = "ステップ番号は1以上である必要があります")
    private Integer stepNumber;

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getStepData() {
        return stepData;
    }

    public void setStepData(String stepData) {
        this.stepData = stepData;
    }

    public Integer getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(Integer stepNumber) {
        this.stepNumber = stepNumber;
    }
}

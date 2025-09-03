package com.ahamo.dummy.demo2.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class StepUpdateRequest {

    @NotNull(message = "ステップ番号は必須です")
    @Min(value = 1, message = "ステップ番号は1以上である必要があります")
    private Integer stepNumber;

    private String stepData;

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
}

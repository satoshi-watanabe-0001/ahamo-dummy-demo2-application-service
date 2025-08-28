package com.ahamo.dummy.demo2.application.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupValidationRequest {
    
    @NotBlank(message = "申し込みIDは必須です")
    private String applicationId;
    
    @NotBlank(message = "ステップデータは必須です")
    private String stepData;
    
    private Integer stepNumber;
}

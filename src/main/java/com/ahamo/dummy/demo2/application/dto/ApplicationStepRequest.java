package com.ahamo.dummy.demo2.application.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationStepRequest {
    
    @NotNull(message = "ステップ番号は必須です")
    @Min(value = 1, message = "ステップ番号は1以上である必要があります")
    @Max(value = 10, message = "ステップ番号は10以下である必要があります")
    private Integer stepNumber;
    
    private String stepData;
}

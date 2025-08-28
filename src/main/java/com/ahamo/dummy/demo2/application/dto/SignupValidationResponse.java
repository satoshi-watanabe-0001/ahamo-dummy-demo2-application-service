package com.ahamo.dummy.demo2.application.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupValidationResponse {
    
    private Boolean isValid;
    private List<String> errors;
    private String nextStep;
    private String message;
}

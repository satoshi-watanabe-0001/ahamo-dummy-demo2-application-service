package com.ahamo.dummy.demo2.application.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationRequest {
    
    @NotBlank(message = "ユーザー名は必須です")
    private String userName;
    
    @NotBlank(message = "メールアドレスは必須です")
    @Email(message = "有効なメールアドレスを入力してください")
    private String email;
    
    @NotBlank(message = "電話番号は必須です")
    @Pattern(regexp = "^[0-9-]+$", message = "有効な電話番号を入力してください")
    private String phone;
    
    private String selectedPlan;
    private String selectedDevice;
    private String selectedOptions;
}

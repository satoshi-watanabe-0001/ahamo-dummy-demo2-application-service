package com.ahamo.dummy.demo2.application.dto;

import com.ahamo.dummy.demo2.application.entity.Application;
import java.time.LocalDateTime;

public class ApplicationResponse {

    private String applicationId;
    private String email;
    private String userName;
    private String phone;
    private String selectedPlan;
    private String selectedDevice;
    private String status;
    private Integer currentStep;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ApplicationResponse() {}

    public ApplicationResponse(Application application) {
        this.applicationId = application.getApplicationId();
        this.email = application.getEmail();
        this.userName = application.getUserName();
        this.phone = application.getPhone();
        this.selectedPlan = application.getSelectedPlan();
        this.selectedDevice = application.getSelectedDevice();
        this.status = application.getStatus().name();
        this.currentStep = application.getCurrentStep();
        this.createdAt = application.getCreatedAt();
        this.updatedAt = application.getUpdatedAt();
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSelectedPlan() {
        return selectedPlan;
    }

    public void setSelectedPlan(String selectedPlan) {
        this.selectedPlan = selectedPlan;
    }

    public String getSelectedDevice() {
        return selectedDevice;
    }

    public void setSelectedDevice(String selectedDevice) {
        this.selectedDevice = selectedDevice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(Integer currentStep) {
        this.currentStep = currentStep;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

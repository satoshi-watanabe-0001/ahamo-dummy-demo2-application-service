package com.ahamo.dummy.demo2.application.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SignupValidationResponse {

    @JsonProperty("isValid")
    private boolean isValid;
    private List<String> errors;
    private String message;
    private Integer nextStep;

    public SignupValidationResponse() {}

    public SignupValidationResponse(boolean isValid, List<String> errors, String message) {
        this.isValid = isValid;
        this.errors = errors;
        this.message = message;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getNextStep() {
        return nextStep;
    }

    public void setNextStep(Integer nextStep) {
        this.nextStep = nextStep;
    }
}

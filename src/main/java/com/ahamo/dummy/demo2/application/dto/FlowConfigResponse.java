package com.ahamo.dummy.demo2.application.dto;

import java.util.Map;

public class FlowConfigResponse {

    private Integer totalSteps;
    private Map<String, StepConfig> steps;
    private String estimatedTotalTime;
    private SupportContact supportContact;

    public FlowConfigResponse() {}

    public Integer getTotalSteps() {
        return totalSteps;
    }

    public void setTotalSteps(Integer totalSteps) {
        this.totalSteps = totalSteps;
    }

    public Map<String, StepConfig> getSteps() {
        return steps;
    }

    public void setSteps(Map<String, StepConfig> steps) {
        this.steps = steps;
    }

    public String getEstimatedTotalTime() {
        return estimatedTotalTime;
    }

    public void setEstimatedTotalTime(String estimatedTotalTime) {
        this.estimatedTotalTime = estimatedTotalTime;
    }

    public SupportContact getSupportContact() {
        return supportContact;
    }

    public void setSupportContact(SupportContact supportContact) {
        this.supportContact = supportContact;
    }

    public static class StepConfig {
        private String name;
        private String description;
        private String estimatedTime;

        public StepConfig() {}

        public StepConfig(String name, String description, String estimatedTime) {
            this.name = name;
            this.description = description;
            this.estimatedTime = estimatedTime;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getEstimatedTime() {
            return estimatedTime;
        }

        public void setEstimatedTime(String estimatedTime) {
            this.estimatedTime = estimatedTime;
        }
    }

    public static class SupportContact {
        private String phone;
        private String email;
        private String hours;

        public SupportContact() {}

        public SupportContact(String phone, String email, String hours) {
            this.phone = phone;
            this.email = email;
            this.hours = hours;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getHours() {
            return hours;
        }

        public void setHours(String hours) {
            this.hours = hours;
        }
    }
}

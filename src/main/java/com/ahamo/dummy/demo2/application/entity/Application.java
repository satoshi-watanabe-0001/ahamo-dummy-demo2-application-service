package com.ahamo.dummy.demo2.application.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Application {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "application_id", unique = true, nullable = false)
    private String applicationId;
    
    @Column(name = "user_name", nullable = false)
    private String userName;
    
    @Column(name = "email", nullable = false)
    private String email;
    
    @Column(name = "phone", nullable = false)
    private String phone;
    
    @Column(name = "selected_plan")
    private String selectedPlan;
    
    @Column(name = "selected_device")
    private String selectedDevice;
    
    @Column(name = "selected_options", columnDefinition = "TEXT")
    private String selectedOptions;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ApplicationStatus status = ApplicationStatus.DRAFT;
    
    @Column(name = "current_step", nullable = false)
    private Integer currentStep = 1;
    
    @Column(name = "total_steps", nullable = false)
    private Integer totalSteps = 5;
    
    @Column(name = "estimated_completion_time")
    private String estimatedCompletionTime;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;
    
    public enum ApplicationStatus {
        DRAFT("下書き"),
        IN_PROGRESS("進行中"),
        SUBMITTED("提出済み"),
        UNDER_REVIEW("審査中"),
        APPROVED("承認済み"),
        REJECTED("却下"),
        COMPLETED("完了");
        
        private final String displayName;
        
        ApplicationStatus(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}

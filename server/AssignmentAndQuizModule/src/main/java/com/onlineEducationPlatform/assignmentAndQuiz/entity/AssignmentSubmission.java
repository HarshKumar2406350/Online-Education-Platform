package com.onlineEducationPlatform.assignmentAndQuiz.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import java.time.LocalDateTime;

@Entity
@Table(name = "assignment_submissions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentSubmission {
    @Id
    @GeneratedValue(generator = "submission-id")
    @GenericGenerator(name = "submission-id", 
                     strategy = "com.onlineEducationPlatform.assignmentAndQuiz.util.AssignmentIdGenerator")
    private String id;  // 8-digit

    @NotBlank(message = "Student ID is required")
    private String studentId;

    @NotBlank(message = "Course ID is required")
    private String courseId;

    @NotBlank(message = "Assignment ID is required")
    private String assignmentId;

    @NotBlank(message = "Answer is required")
    @Column(columnDefinition = "TEXT")
    private String answer;

    @Builder.Default
    private Boolean checked = false;

    @Builder.Default
    private Integer marksObtained = 0;

    private LocalDateTime submittedAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        submittedAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
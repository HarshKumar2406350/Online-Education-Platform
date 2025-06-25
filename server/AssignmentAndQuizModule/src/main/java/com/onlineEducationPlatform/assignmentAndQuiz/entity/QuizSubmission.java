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
@Table(name = "quiz_submissions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizSubmission {
    @Id
    @GeneratedValue(generator = "quiz-submission-id")
    @GenericGenerator(name = "quiz-submission-id", 
                     strategy = "com.onlineEducationPlatform.assignmentAndQuiz.util.QuizIdGenerator")
    private String id;  // 9-digit

    @NotBlank(message = "Student ID is required")
    private String studentId;

    @NotBlank(message = "Course ID is required")
    private String courseId;

    @NotBlank(message = "Quiz ID is required")
    private String quizId;

    @NotBlank(message = "Answer option is required")
    private String answerOption;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private SubmissionStatus status = SubmissionStatus.PENDING;
    
    public enum SubmissionStatus {
        PENDING, CHECKED
    }

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
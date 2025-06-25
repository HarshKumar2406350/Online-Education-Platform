package com.onlineEducationPlatform.assignmentAndQuiz.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "quizzes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Quiz {
    @Id
    @GeneratedValue(generator = "quiz-id")
    @GenericGenerator(name = "quiz-id", 
                     strategy = "com.onlineEducationPlatform.assignmentAndQuiz.util.QuizIdGenerator")
    private String id;  // 9-digit

    @NotBlank(message = "Course ID is required")
    private String courseId;

    @NotBlank(message = "Question is required")
    @Column(columnDefinition = "TEXT")
    private String question;

    @ElementCollection
    @CollectionTable(name = "quiz_options",
                    joinColumns = @JoinColumn(name = "quiz_id"))
    @Column(name = "answer_option")
    @Size(min = 4, max = 4, message = "Quiz must have exactly 4 options")
    private List<String> answerOptions;

    @NotBlank(message = "Correct answer is required")
    private String correctAnswer;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
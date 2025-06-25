package com.onlineEducationPlatform.assignmentAndQuiz.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizResponse {
    private String id;
    private String courseId;
    private String question;
    private List<String> answerOptions;
    private String correctAnswer;  // Only included for instructors
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isAttempted;  // For student view
}
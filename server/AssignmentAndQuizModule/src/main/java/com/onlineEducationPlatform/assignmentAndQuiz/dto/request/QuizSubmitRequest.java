package com.onlineEducationPlatform.assignmentAndQuiz.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizSubmitRequest {

    @NotBlank(message = "Student ID is required")
    private String studentId;

    @NotBlank(message = "Course ID is required")
    private String courseId;

    @NotBlank(message = "Quiz ID is required")
    private String quizId;

    @NotBlank(message = "Answer option is required")
    private String answerOption;
}
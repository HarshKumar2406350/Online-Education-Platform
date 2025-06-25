package com.onlineEducationPlatform.assignmentAndQuiz.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizRequest {

    @NotBlank(message = "Course ID is required")
    private String courseId;

    @NotBlank(message = "Question is required")
    @Size(min = 10, max = 1000, message = "Question must be between 10 and 1000 characters")
    private String question;

    @NotEmpty(message = "Answer options are required")
    @Size(min = 4, max = 4, message = "Exactly 4 answer options are required")
    private List<String> answerOptions;

    @NotBlank(message = "Correct answer is required")
    private String correctAnswer;
}

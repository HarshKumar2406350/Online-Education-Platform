package com.onlineEducationPlatform.assignmentAndQuiz.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentSubmitRequest {

    @NotBlank(message = "Student ID is required")
    private String studentId;

    @NotBlank(message = "Course ID is required")
    private String courseId;

    @NotBlank(message = "Assignment ID is required")
    private String assignmentId;

    @NotBlank(message = "Answer cannot be empty")
    @Size(min = 10, max = 10000, message = "Answer must be between 10 and 10000 characters")
    private String answer;
}
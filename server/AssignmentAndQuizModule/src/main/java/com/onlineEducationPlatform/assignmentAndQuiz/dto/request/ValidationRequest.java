package com.onlineEducationPlatform.assignmentAndQuiz.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ValidationRequest {

    @NotBlank(message = "Course ID is required")
    private String courseId;

    @NotBlank(message = "User ID is required")
    private String userId;
}

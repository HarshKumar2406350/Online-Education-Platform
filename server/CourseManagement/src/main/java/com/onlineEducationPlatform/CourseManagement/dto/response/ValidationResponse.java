package com.onlineEducationPlatform.CourseManagement.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ValidationResponse {

    @NotBlank(message = "Course ID is required")
    private boolean isValid;

    private CourseResponse  course;

    private EnrollmentResponse enrollment;
}

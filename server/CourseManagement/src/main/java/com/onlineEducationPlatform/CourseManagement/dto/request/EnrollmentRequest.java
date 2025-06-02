package com.onlineEducationPlatform.CourseManagement.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EnrollmentRequest {
    
    @NotBlank(message = "Course ID is required")
    private String courseId;
    
    @NotBlank(message = "Student ID is required")
    private String studentId;
}
package com.onlineEducationPlatform.CourseManagement.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EnrollmentResponse {
    private String id;
    private String courseId;
    private String studentId;
    private LocalDateTime enrollmentDate;
    private CourseResponse course;
}
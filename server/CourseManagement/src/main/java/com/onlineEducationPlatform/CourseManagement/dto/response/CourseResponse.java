package com.onlineEducationPlatform.CourseManagement.dto.response;


import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CourseResponse {
    private String id;
    private String title;
    private String description;
    private String instructorId;
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
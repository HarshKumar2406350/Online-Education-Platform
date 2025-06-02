package com.onlineEducationPlatform.CourseManagement.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseDeleteResponse {
    private String message;
    private CourseResponse course;
}

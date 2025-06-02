package com.onlineEducationPlatform.CourseManagement.mapper;

import com.onlineEducationPlatform.CourseManagement.dto.request.CourseRequest;
import com.onlineEducationPlatform.CourseManagement.dto.response.CourseResponse;
import com.onlineEducationPlatform.CourseManagement.entity.Course;
import com.onlineEducationPlatform.CourseManagement.entity.CourseCategory;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {
    
    public Course toEntity(CourseRequest request, String instructorId) {
        return Course.builder()
            .title(request.getTitle())
            .description(request.getDescription())
            .category(CourseCategory.valueOf(request.getCategory().toUpperCase()))
            .instructorId(instructorId)
            .build();
    }
    
    public CourseResponse toResponse(Course course) {
        CourseResponse response = new CourseResponse();
        response.setId(course.getId());
        response.setTitle(course.getTitle());
        response.setDescription(course.getDescription());
        response.setInstructorId(course.getInstructorId());
        response.setCategory(course.getCategory().name());
        response.setCreatedAt(course.getCreatedAt());
        response.setUpdatedAt(course.getUpdatedAt());
        return response;
    }
    
    public void updateEntity(Course course, CourseRequest request) {
        if (request.getTitle() != null) {
            course.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            course.setDescription(request.getDescription());
        }
        if (request.getCategory() != null) {
            course.setCategory(CourseCategory.valueOf(request.getCategory().toUpperCase()));
        }
    }
}

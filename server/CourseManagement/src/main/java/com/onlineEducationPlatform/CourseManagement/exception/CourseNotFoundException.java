package com.onlineEducationPlatform.CourseManagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CourseNotFoundException extends RuntimeException {
    
    public CourseNotFoundException(String courseId) {
        super("Course not found with ID: " + courseId);
    }
}
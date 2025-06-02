package com.onlineEducationPlatform.CourseManagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateEnrollmentException extends RuntimeException {
    
    public DuplicateEnrollmentException(String studentId, String courseId) {
        super("Student " + studentId + " is already enrolled in course " + courseId);
    }
}
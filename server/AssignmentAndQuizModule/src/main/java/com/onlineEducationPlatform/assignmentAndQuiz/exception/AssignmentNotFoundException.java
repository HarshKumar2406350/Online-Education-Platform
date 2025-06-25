package com.onlineEducationPlatform.assignmentAndQuiz.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AssignmentNotFoundException extends RuntimeException {
    public AssignmentNotFoundException(String assignmentId) {
        super("Assignment not found with id: " + assignmentId);
    }

    public AssignmentNotFoundException(String assignmentId, String courseId) {
        super("Assignment not found with id: " + assignmentId + " in course: " + courseId);
    }
}
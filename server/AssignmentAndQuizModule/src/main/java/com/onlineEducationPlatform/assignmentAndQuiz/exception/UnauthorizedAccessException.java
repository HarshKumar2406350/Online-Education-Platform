package com.onlineEducationPlatform.assignmentAndQuiz.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException(String message) {
        super(message);
    }

    public UnauthorizedAccessException(String resourceType, String resourceId, String userId) {
        super("User " + userId + " is not authorized to access " + resourceType + " with id: " + resourceId);
    }
}
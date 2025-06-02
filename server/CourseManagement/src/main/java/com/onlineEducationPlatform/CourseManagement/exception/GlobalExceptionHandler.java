package com.onlineEducationPlatform.CourseManagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCourseNotFoundException(CourseNotFoundException ex) {
        return createErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(DuplicateEnrollmentException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEnrollmentException(DuplicateEnrollmentException ex) {
        return createErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedAccessException(UnauthorizedAccessException ex) {
        return createErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    private ResponseEntity<ErrorResponse> createErrorResponse(HttpStatus status, String message) {
        ErrorResponse errorResponse = new ErrorResponse(
            status.value(),
            status.getReasonPhrase(),
            message,
            LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, status);
    }

    private record ErrorResponse(
        int status,
        String error,
        String message,
        LocalDateTime timestamp
    ) {}
}
package com.onlineEducationPlatform.CourseManagement.controller;

import com.onlineEducationPlatform.CourseManagement.dto.request.ValidationRequest;
import com.onlineEducationPlatform.CourseManagement.dto.response.EnrollmentResponse;
import com.onlineEducationPlatform.CourseManagement.dto.response.ValidationResponse;
import com.onlineEducationPlatform.CourseManagement.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/CourseManagement/enrollments")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping("/{courseId}/enroll")
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public ResponseEntity<Map<String, Object>>  enrollInCourse(
            @PathVariable String courseId,
            Authentication authentication) {
        EnrollmentResponse enrollment = enrollmentService.enrollInCourse(courseId, authentication.getName());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Enrolled in course successfully");
        response.put("data", enrollment);
        response.put("status", HttpStatus.CREATED.value());


        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{courseId}/unenroll")
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public ResponseEntity<Map<String, Object>>  unenrollFromCourse(
            @PathVariable String courseId,
            Authentication authentication) {
        enrollmentService.unenrollFromCourse(courseId, authentication.getName());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Unenrolled from course successfully");
        response.put("status", HttpStatus.NO_CONTENT.value());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/student")
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public ResponseEntity<Map<String, Object>>  getStudentEnrollments(Authentication authentication) {
        List<EnrollmentResponse> enrollments = enrollmentService.getEnrollmentsByStudent(authentication.getName());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Enrollments found successfully");
        response.put("data", enrollments);
        response.put("status", HttpStatus.OK.value());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public ResponseEntity<Map<String, Object>> getCourseEnrollments(
            @PathVariable String courseId,
            Authentication authentication) {

        List<EnrollmentResponse> enrollments = enrollmentService.getEnrollmentsByCourse(courseId, authentication.getName());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Enrollments found successfully");
        response.put("data", enrollments);
        response.put("status", HttpStatus.OK.value());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateEnrollment(
            @RequestBody @Valid ValidationRequest request,
            Authentication authentication) {

        ValidationResponse validationResponse = enrollmentService.isEnrolled(request.getCourseId(), request.getUserId());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Validation successful");
        response.put("data", validationResponse);
        response.put("status", HttpStatus.OK.value());

        return ResponseEntity.ok(response);
    }
}
package com.onlineEducationPlatform.CourseManagement.controller;

import com.onlineEducationPlatform.CourseManagement.dto.response.EnrollmentResponse;
import com.onlineEducationPlatform.CourseManagement.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/CourseManagement/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping("/{courseId}/enroll")
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public ResponseEntity<EnrollmentResponse> enrollInCourse(
            @PathVariable String courseId,
            Authentication authentication) {
        EnrollmentResponse response = enrollmentService.enrollInCourse(courseId, authentication.getName());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{courseId}/unenroll")
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public ResponseEntity<Void> unenrollFromCourse(
            @PathVariable String courseId,
            Authentication authentication) {
        enrollmentService.unenrollFromCourse(courseId, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/student")
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public ResponseEntity<List<EnrollmentResponse>> getStudentEnrollments(Authentication authentication) {
        List<EnrollmentResponse> enrollments = enrollmentService.getEnrollmentsByStudent(authentication.getName());
        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public ResponseEntity<List<EnrollmentResponse>> getCourseEnrollments(
            @PathVariable String courseId,
            Authentication authentication) {
        List<EnrollmentResponse> enrollments = enrollmentService.getEnrollmentsByCourse(courseId, authentication.getName());
        return ResponseEntity.ok(enrollments);
    }
}
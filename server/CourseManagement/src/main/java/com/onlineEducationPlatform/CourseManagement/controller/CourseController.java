package com.onlineEducationPlatform.CourseManagement.controller;

import com.onlineEducationPlatform.CourseManagement.dto.request.CourseRequest;
import com.onlineEducationPlatform.CourseManagement.dto.response.CourseResponse;
import com.onlineEducationPlatform.CourseManagement.dto.response.CourseDeleteResponse;
import com.onlineEducationPlatform.CourseManagement.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/CourseManagement")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class CourseController {

    private final CourseService courseService;

    @PostMapping("/courses")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public ResponseEntity<CourseResponse> createCourse(
            @Valid @RequestBody CourseRequest request,
            Authentication authentication) {
        CourseResponse response = courseService.createCourse(request, authentication.getName());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/courses/{courseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public ResponseEntity<CourseResponse> updateCourse(
            @PathVariable String courseId,
            @Valid @RequestBody CourseRequest request,
            Authentication authentication) {
        CourseResponse response = courseService.updateCourse(courseId, request, authentication.getName());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/courses/{courseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public ResponseEntity<CourseDeleteResponse> deleteCourse(
            @PathVariable String courseId,
            Authentication authentication) {
        CourseDeleteResponse response = courseService.deleteCourse(courseId, authentication.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/courses/{courseId}")
    public ResponseEntity<CourseResponse> getCourse(@PathVariable String courseId) {
        CourseResponse response = courseService.getCourseById(courseId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/courses")
    public ResponseEntity<List<CourseResponse>> getAllCourses() {
        List<CourseResponse> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/courses/instructor")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public ResponseEntity<List<CourseResponse>> getInstructorCourses(Authentication authentication) {
        List<CourseResponse> courses = courseService.getCoursesByInstructor(authentication.getName());
        return ResponseEntity.ok(courses);
    }
}
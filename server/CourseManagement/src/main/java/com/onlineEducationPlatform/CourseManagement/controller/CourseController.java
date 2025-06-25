package com.onlineEducationPlatform.CourseManagement.controller;

import com.onlineEducationPlatform.CourseManagement.dto.request.CourseRequest;
import com.onlineEducationPlatform.CourseManagement.dto.request.ValidationRequest;
import com.onlineEducationPlatform.CourseManagement.dto.response.CourseResponse;
import com.onlineEducationPlatform.CourseManagement.dto.response.CourseDeleteResponse;
import com.onlineEducationPlatform.CourseManagement.dto.response.ValidationResponse;
import com.onlineEducationPlatform.CourseManagement.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;

@RestController
@RequestMapping("/api/CourseManagement/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public ResponseEntity<Map<String, Object>> createCourse(
            @Valid @RequestBody CourseRequest request,
            Authentication authentication) {
        CourseResponse createdCourse = courseService.createCourse(request, authentication.getName());

        Map<String, Object> response = new HashMap<>();

        response.put("message", "Course created successfully");
        response.put("data", createdCourse);
        response.put("status", HttpStatus.CREATED.value());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{courseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public ResponseEntity<Map<String, Object>> updateCourse(
            @PathVariable String courseId,
            @Valid @RequestBody CourseRequest request,
            Authentication authentication) {
        CourseResponse updatedCourse = courseService.updateCourse(courseId, request, authentication.getName());
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Course updated successfully");
        response.put("data", updatedCourse);
        response.put("status", HttpStatus.OK.value());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{courseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public ResponseEntity<Map<String, Object>> deleteCourse(
            @PathVariable String courseId,
            Authentication authentication) {
        CourseDeleteResponse deletedCourse = courseService.deleteCourse(courseId, authentication.getName());

        Map<String, Object> response = new HashMap<>();

        response.put("message", "Course deleted successfully");
        response.put("data", deletedCourse);
        response.put("status", HttpStatus.OK.value());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Map<String, Object>> getCoursebyId(@PathVariable String courseId) {
        CourseResponse foundCourse = courseService.getCourseById(courseId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Course found successfully");
        response.put("data", foundCourse);
        response.put("status", HttpStatus.OK.value());

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllCourses() {
        List<CourseResponse> courses = courseService.getAllCourses();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Courses found successfully");
        response.put("data", courses);
        response.put("status", HttpStatus.OK.value());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/instructor")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public ResponseEntity<Map<String, Object>> getInstructorCourses(Authentication authentication) {

        List<CourseResponse> courses = courseService.getCoursesByInstructor(authentication.getName());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Courses found successfully");
        response.put("data", courses);
        response.put("status", HttpStatus.OK.value());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateCourseOwnership(
            @RequestBody @Valid ValidationRequest request,
            Authentication authentication) {
        logger.info("Validating course ownership for courseId: {}, userId: {}", request.getCourseId(), request.getUserId());
        ValidationResponse validationResponse = courseService.ValidateCourseOwnership(request.getCourseId(),request.getUserId());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Course found successfully");
        response.put("validation", validationResponse);
        response.put("status", HttpStatus.OK.value());

        return ResponseEntity.ok(response);
    }
}
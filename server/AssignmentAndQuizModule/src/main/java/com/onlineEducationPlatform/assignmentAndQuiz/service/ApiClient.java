package com.onlineEducationPlatform.assignmentAndQuiz.service;

import com.onlineEducationPlatform.assignmentAndQuiz.dto.request.ValidationRequest;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;

@FeignClient(name= "CourseManagement", url = "http://localhost:8081")
public interface ApiClient {
    @PostMapping("api/CourseManagement/api/CourseManagement/courses/validate")
    Map<String, Object> validateCourseOwnership(
            @RequestBody ValidationRequest request);

    @PostMapping("api/CourseManagement/api/CourseManagement/enrollments/validate")
    Map<String, Object> validateEnrollment(
            @RequestBody ValidationRequest request);
}

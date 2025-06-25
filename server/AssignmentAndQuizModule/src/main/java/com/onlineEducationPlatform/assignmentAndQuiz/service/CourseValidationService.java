package com.onlineEducationPlatform.assignmentAndQuiz.service;

import com.onlineEducationPlatform.assignmentAndQuiz.dto.request.ValidationRequest;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseValidationService {
    
    @Value("${course.service.url}")
    private String courseServiceUrl;

    @Autowired
    private final ApiClient apiClient;

    private static final Logger logger = LoggerFactory.getLogger(CourseValidationService.class);


    public boolean validateCourseAccess(String courseId, String userId) {
        log.debug("Validating course access for courseId: {} and userId: {}", courseId, userId);
        logger.info("Validating course access for courseId: {} and userId: {}", courseId, userId);
         try {
             // Call Course Management API
             ValidationRequest  request = new ValidationRequest(courseId, userId);
             Map<String, Object> response = apiClient.validateCourseOwnership(request);
             logger.info("Course access validation response: {}", response);
             Map<String, Object> validation = (Map<String, Object>) response.get("validation");
             logger.info("Course access validation: {}", validation);
             if (validation != null && Boolean.TRUE.equals(validation.get("valid"))) {
                 return true;
             }
            return false;
         } catch (Exception e) {
             throw new RuntimeException("Failed to validate course access: " + e.getMessage());
         }
    }

    public boolean validateUserEnrollment(String courseId, String userId) {
        log.debug("Validating user enrollment for courseId: {} and userId: {}", courseId, userId);
        logger.info("Validating user enrollment for courseId: {} and userId: {}", courseId, userId);
         try {
             // Call Course Management API
             ValidationRequest request = new ValidationRequest(courseId, userId);

             Map<String, Object> response = apiClient.validateEnrollment(request);


             logger.info("User enrollment validation response: {}", response);


             Map<String, Boolean> validation = (Map<String, Boolean>) response.get("data");


             logger.info("User enrollment validation: {}", validation);


             if (validation != null && validation.containsKey("isEnrolled")) {
                 return validation.get("isEnrolled");
             }
             return false;
         } catch (Exception e) {
             throw new RuntimeException("Failed to validate enrollment: " + e.getMessage());
         }

    }
}
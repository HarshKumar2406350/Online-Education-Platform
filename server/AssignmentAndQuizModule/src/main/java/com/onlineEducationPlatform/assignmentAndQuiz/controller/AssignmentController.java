package com.onlineEducationPlatform.assignmentAndQuiz.controller;

import com.onlineEducationPlatform.assignmentAndQuiz.dto.request.AssignmentRequest;
import com.onlineEducationPlatform.assignmentAndQuiz.dto.response.ApiResponse;
import com.onlineEducationPlatform.assignmentAndQuiz.dto.response.AssignmentResponse;
import com.onlineEducationPlatform.assignmentAndQuiz.service.AssignmentService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/assignments")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class AssignmentController {

    private final AssignmentService assignmentService;

    @PostMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<ApiResponse<AssignmentResponse>> createAssignment(
            @Valid @RequestBody AssignmentRequest request,
            Authentication authentication) {
        // Add null check and proper error handling
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.<AssignmentResponse>builder()
                    .message("User not authenticated")
                    .statusCode(401)
                    .build());
        }

        AssignmentResponse response = assignmentService.createAssignment(
            request, 
            authentication.getName()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.<AssignmentResponse>builder()
                .message("Assignment created successfully")
                .statusCode(201)
                .body(response)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AssignmentResponse>> updateAssignment(
            @PathVariable String id,
            @Valid @RequestBody AssignmentRequest request,
            Authentication authentication) {
    	
    		AssignmentResponse response = assignmentService.updateAssignment(id, request, authentication.getName());
    	
       
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<AssignmentResponse>builder()
                    .message("Assignment UPDATED  successfully")
                    .statusCode(200)
                    .body(response)
                    .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AssignmentResponse>> getAssignment(
            @PathVariable String id,
            Authentication authentication) {

                AssignmentResponse response = assignmentService.getAssignment(id, authentication.getName());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<AssignmentResponse>builder()
                    .message("Assignment retrieved successfully")
                    .statusCode(200)
                    .body(response)
                    .build());
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse<List<AssignmentResponse>>> getAssignmentsByCourse(
            @PathVariable String courseId,
            Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.<List<AssignmentResponse>>builder()
                    .message("User not authenticated")
                    .statusCode(401)
                    .build());
        }
        
        List<AssignmentResponse> assignments = assignmentService.getAssignmentsByCourse(courseId, authentication.getName());
        
        return ResponseEntity.ok(ApiResponse.<List<AssignmentResponse>>builder()
            .message("Assignments retrieved successfully")
            .statusCode(200)
            .body(assignments)
            .build());
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(
            @PathVariable String id,
            Authentication authentication) {
        assignmentService.deleteAssignment(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> adminDeleteAssignment(
            @PathVariable String id,
            @RequestParam String courseId,
            @RequestParam String creatorId) {
        assignmentService.adminDeleteAssignment(id, courseId, creatorId);
        
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .message("Assignment deleted successfully")
                .statusCode(200)
                .body(null)
                .build());
    }
}

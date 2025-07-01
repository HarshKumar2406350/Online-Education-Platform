package com.onlineEducationPlatform.assignmentAndQuiz.controller;

import com.onlineEducationPlatform.assignmentAndQuiz.dto.request.AssignmentSubmitRequest;
import com.onlineEducationPlatform.assignmentAndQuiz.dto.response.ApiResponse;
import com.onlineEducationPlatform.assignmentAndQuiz.dto.response.AssignmentSubmitResponse;
import com.onlineEducationPlatform.assignmentAndQuiz.service.AssignmentSubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/assignment-submissions")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class AssignmentSubmissionController {

    private final AssignmentSubmissionService submissionService;


    @PutMapping("/{submissionId}/grade")
    public ResponseEntity<AssignmentSubmitResponse> gradeAssignment(
            @PathVariable String submissionId,
            @RequestParam Integer marks,
            Authentication authentication) {
        return ResponseEntity.ok(submissionService.gradeAssignment(submissionId, marks, authentication.getName()));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<AssignmentSubmitResponse>> getSubmissionsByCourse(
            @PathVariable String courseId,
            Authentication authentication) {
        return ResponseEntity.ok(submissionService.getSubmissionsByCourse(courseId, authentication.getName()));
    }

    @GetMapping("/student/{studentId}/course/{courseId}")
    public ResponseEntity<List<AssignmentSubmitResponse>> getSubmissionsByStudent(
            @PathVariable String studentId,
            @PathVariable String courseId) {
        return ResponseEntity.ok(submissionService.getSubmissionsByStudent(studentId, courseId));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AssignmentSubmitResponse>> submitAssignment(
            @Valid @RequestBody AssignmentSubmitRequest request) {
        return ResponseEntity.ok(ApiResponse.<AssignmentSubmitResponse>builder()
                .message("Assignment submitted successfully")
                .statusCode(201)
                .body(submissionService.submitAssignment(request))
                .build());
    }

    @PutMapping("/{submissionId}")
    public ResponseEntity<ApiResponse<AssignmentSubmitResponse>> updateAssignmentSubmission(
            @PathVariable String submissionId,
            @Valid @RequestBody AssignmentSubmitRequest request) {
        return ResponseEntity.ok(ApiResponse.<AssignmentSubmitResponse>builder()
                .message("Assignment submission updated successfully")
                .statusCode(200)
                .body(submissionService.updateAssignmentSubmission(submissionId, request))
                .build());
    }

    @DeleteMapping("/admin/{submissionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteSubmission(
            @PathVariable String submissionId) {
        submissionService.deleteSubmission(submissionId);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .message("Assignment submission deleted successfully")
                .statusCode(200)
                .body(null)
                .build());
    }

    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<AssignmentSubmitResponse>>> getAllSubmissions() {
        return ResponseEntity.ok(ApiResponse.<List<AssignmentSubmitResponse>>builder()
                .message("All submissions fetched successfully")
                .statusCode(200)
                .body(submissionService.getAllSubmissions())
                .build());
    }


}
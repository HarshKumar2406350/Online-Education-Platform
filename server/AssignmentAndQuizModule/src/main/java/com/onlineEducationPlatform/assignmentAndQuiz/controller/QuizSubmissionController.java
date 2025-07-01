package com.onlineEducationPlatform.assignmentAndQuiz.controller;

import com.onlineEducationPlatform.assignmentAndQuiz.dto.request.QuizSubmitRequest;
import com.onlineEducationPlatform.assignmentAndQuiz.dto.response.ApiResponse;
import com.onlineEducationPlatform.assignmentAndQuiz.dto.response.QuizSubmitResponse;
import com.onlineEducationPlatform.assignmentAndQuiz.entity.QuizSubmission;
import com.onlineEducationPlatform.assignmentAndQuiz.mapper.QuizSubmissionMapper;
import com.onlineEducationPlatform.assignmentAndQuiz.service.QuizSubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/quiz-submissions")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class QuizSubmissionController {

    private final QuizSubmissionService submissionService;

    @PostMapping
    public ResponseEntity<ApiResponse<QuizSubmitResponse>> submitQuiz(
            @Valid @RequestBody QuizSubmitRequest request) {
        return ResponseEntity.ok(ApiResponse.<QuizSubmitResponse>builder()
                .message("Quiz submitted successfully")
                .statusCode(201)
                .body(submissionService.submitQuiz(request))
                .build());
    }

    @DeleteMapping("/admin/{submissionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteSubmission(
            @PathVariable String submissionId) {
        submissionService.deleteSubmission(submissionId);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .message("Quiz submission deleted successfully")
                .statusCode(200)
                .body(null)
                .build());
    }


    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse<List<QuizSubmitResponse>>> getSubmissionsByCourse(
            @PathVariable String courseId,
            Authentication authentication) {
                return ResponseEntity.ok(ApiResponse.<List<QuizSubmitResponse>>builder()
                .message("Submissions retrieved successfully")
                .statusCode(200)
                .body(submissionService.getSubmissionsByCourse(courseId, authentication.getName()))
                .build());
    }


    @GetMapping("/student/{studentId}/course/{courseId}/quiz/{quizId}/marks")
    public ResponseEntity<ApiResponse<Integer>> getMarksByStudentIdAndCourseIdAndQuizId(
            @PathVariable String studentId,
            @PathVariable String courseId,
            @PathVariable String quizId) {
        Optional<Integer> marks = submissionService.getMarksByStudentIdAndCourseIdAndQuizId(studentId, courseId, quizId);
        if (marks.isPresent()) {
            return ResponseEntity.ok(ApiResponse.<Integer>builder()
                    .message("Marks retrieved successfully")
                    .statusCode(200)
                    .body(marks.get())
                    .build());
        } else {
            return ResponseEntity.ok(ApiResponse.<Integer>builder()
                    .message("No submission found for the given details")
                    .statusCode(404)
                    .body(null)
                    .build());
        }
    }


    @GetMapping("/student/{studentId}/course/{courseId}")
    public ResponseEntity<ApiResponse<List<QuizSubmitResponse>>> getSubmissionsByStudent(
        @PathVariable String studentId,
        @PathVariable String courseId) {
    return ResponseEntity.ok(ApiResponse.<List<QuizSubmitResponse>>builder()
            .message("Student submissions retrieved successfully")
            .statusCode(200)
            .body(submissionService.getSubmissionsByStudent(studentId, courseId))
            .build());
}

    @GetMapping("/{submissionId}")
    public ResponseEntity<QuizSubmitResponse> getSubmissionResult(
            @PathVariable String submissionId,
            Authentication authentication) {
        return ResponseEntity.ok(submissionService.getSubmissionResult(submissionId, authentication.getName()));
    }

    @GetMapping("/course/{courseId}/pending")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<ApiResponse<List<QuizSubmitResponse>>> getPendingSubmissions(
            @PathVariable String courseId,
            Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.<List<QuizSubmitResponse>>builder()
                .message("Pending submissions retrieved successfully")
                .statusCode(200)
                .body(submissionService.getPendingSubmissionsByCourse(courseId, authentication.getName()))
                .build());
    }

    @GetMapping("/course/{courseId}/checked")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<ApiResponse<List<QuizSubmitResponse>>> getCheckedSubmissions(
            @PathVariable String courseId,
            Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.<List<QuizSubmitResponse>>builder()
                .message("Checked submissions retrieved successfully")
                .statusCode(200)
                .body(submissionService.getCheckedSubmissionsByCourse(courseId, authentication.getName()))
                .build());
    }
    
    @PutMapping("/{submissionId}/answer")
    public ResponseEntity<ApiResponse<QuizSubmitResponse>> updateQuizAnswer(
            @PathVariable String submissionId,
            @Valid @RequestBody QuizSubmitRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.<QuizSubmitResponse>builder()
                .message("Quiz answer updated successfully")
                .statusCode(200)
                .body(submissionService.updateAnswer(submissionId, request, authentication.getName()))
                .build());
    }
}
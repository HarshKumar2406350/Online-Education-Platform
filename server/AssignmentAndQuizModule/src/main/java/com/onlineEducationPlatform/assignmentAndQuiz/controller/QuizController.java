package com.onlineEducationPlatform.assignmentAndQuiz.controller;

import com.onlineEducationPlatform.assignmentAndQuiz.dto.request.QuizRequest;
import com.onlineEducationPlatform.assignmentAndQuiz.dto.response.QuizResponse;
import com.onlineEducationPlatform.assignmentAndQuiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.onlineEducationPlatform.assignmentAndQuiz.dto.response.ApiResponse;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/quizzes")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @PostMapping
    public ResponseEntity<ApiResponse<QuizResponse>> createQuiz(
            @Valid @RequestBody QuizRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.<QuizResponse>builder()
                .message("Quiz created successfully")
                .statusCode(201)
                .body(quizService.createQuiz(request, authentication.getName()))
                .build());
    }

    @PostMapping("/batch")
    public ResponseEntity<ApiResponse<List<QuizResponse>>> batchCreateQuiz(
            @Valid @RequestBody List<QuizRequest> requests,
            Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.<List<QuizResponse>>builder()
                .message("Quizzes created successfully")
                .statusCode(201)
                .body(quizService.batchCreateQuiz(requests, authentication.getName()))
                .build());
    }

    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> adminDeleteQuiz(
            @PathVariable String id,
            @RequestParam String courseId,
            @RequestParam String creatorId) {
        quizService.adminDeleteQuiz(id, courseId, creatorId);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .message("Quiz deleted successfully")
                .statusCode(200)
                .body(null)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuizResponse> updateQuiz(
            @PathVariable String id,
            @Valid @RequestBody QuizRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(quizService.updateQuiz(id, request, authentication.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizResponse> getQuiz(
            @PathVariable String id,
            Authentication authentication) {
        return ResponseEntity.ok(quizService.getQuiz(id, authentication.getName()));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<QuizResponse>> getQuizzesByCourse(
            @PathVariable String courseId,
            Authentication authentication) {
        return ResponseEntity.ok(quizService.getQuizzesByCourse(courseId, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuiz(
            @PathVariable String id,
            Authentication authentication) {
        quizService.deleteQuiz(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
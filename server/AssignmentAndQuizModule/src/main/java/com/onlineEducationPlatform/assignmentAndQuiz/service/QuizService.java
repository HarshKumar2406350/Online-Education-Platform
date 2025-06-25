package com.onlineEducationPlatform.assignmentAndQuiz.service;

import com.onlineEducationPlatform.assignmentAndQuiz.dto.request.QuizRequest;
import com.onlineEducationPlatform.assignmentAndQuiz.dto.response.QuizResponse;
import java.util.List;

public interface QuizService {
    QuizResponse createQuiz(QuizRequest request, String instructorId);
    QuizResponse updateQuiz(String id, QuizRequest request, String instructorId);
    QuizResponse getQuiz(String id, String userId);
    List<QuizResponse> getQuizzesByCourse(String courseId, String userId);
    void deleteQuiz(String id, String instructorId);
    /**
     * Admin endpoint to delete a quiz
     */
    void adminDeleteQuiz(String id, String courseId, String creatorId);

    /**
     * Create multiple quizzes in batch
     */
    List<QuizResponse> batchCreateQuiz(List<QuizRequest> requests, String instructorId);
}
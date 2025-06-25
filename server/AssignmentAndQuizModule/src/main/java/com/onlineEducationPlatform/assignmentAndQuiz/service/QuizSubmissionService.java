package com.onlineEducationPlatform.assignmentAndQuiz.service;

import com.onlineEducationPlatform.assignmentAndQuiz.dto.request.QuizSubmitRequest;
import com.onlineEducationPlatform.assignmentAndQuiz.dto.response.QuizSubmitResponse;
import com.onlineEducationPlatform.assignmentAndQuiz.entity.QuizSubmission;

import java.util.List;

public interface QuizSubmissionService {
    QuizSubmitResponse submitQuiz(QuizSubmitRequest request);

    List<QuizSubmitResponse> getSubmissionsByCourse(String courseId, String instructorId);

    List<QuizSubmitResponse> getSubmissionsByStudent(String studentId, String courseId);

    QuizSubmitResponse getSubmissionResult(String submissionId, String userId);
    
    void deleteSubmission(String submissionId);

    QuizSubmitResponse updateAnswer(String submissionId, QuizSubmitRequest request, String studentId);

    List<QuizSubmitResponse> getPendingSubmissionsByCourse(String courseId, String instructorId);
    
    List<QuizSubmitResponse> getCheckedSubmissionsByCourse(String courseId, String instructorId);
}
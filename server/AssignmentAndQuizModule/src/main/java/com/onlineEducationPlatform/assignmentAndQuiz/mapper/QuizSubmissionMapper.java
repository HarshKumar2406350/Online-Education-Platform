package com.onlineEducationPlatform.assignmentAndQuiz.mapper;

import com.onlineEducationPlatform.assignmentAndQuiz.dto.request.QuizSubmitRequest;
import com.onlineEducationPlatform.assignmentAndQuiz.dto.response.QuizSubmitResponse;
import com.onlineEducationPlatform.assignmentAndQuiz.entity.QuizSubmission;
import org.springframework.stereotype.Component;

@Component
public class QuizSubmissionMapper {


    public QuizSubmission toEntity(QuizSubmitRequest request, boolean result) {
        return QuizSubmission.builder()
                .studentId(request.getStudentId())
                .courseId(request.getCourseId())
                .quizId(request.getQuizId())
                .answerOption(request.getAnswerOption())
                .marksObtained(result ? 1 : 0)
                .status(QuizSubmission.SubmissionStatus.PENDING)  // Add default status
                .build();
    }

    public QuizSubmitResponse toResponse(QuizSubmission submission) {

        if (submission == null) {
            return null;
        }

        return QuizSubmitResponse.builder()
                .id(submission.getId())
                .studentId(submission.getStudentId())
                .courseId(submission.getCourseId())
                .quizId(submission.getQuizId())
                .answerOption(submission.getAnswerOption())
                .marksObtained(submission.getMarksObtained())
                .submittedAt(submission.getSubmittedAt())
                .updatedAt(submission.getUpdatedAt())
                .status(submission.getStatus().toString())  // Use enum status
                .build();
    }

}
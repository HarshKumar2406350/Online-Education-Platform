package com.onlineEducationPlatform.assignmentAndQuiz.mapper;

import com.onlineEducationPlatform.assignmentAndQuiz.dto.request.AssignmentSubmitRequest;
import com.onlineEducationPlatform.assignmentAndQuiz.dto.response.AssignmentSubmitResponse;
import com.onlineEducationPlatform.assignmentAndQuiz.entity.AssignmentSubmission;
import org.springframework.stereotype.Component;

@Component
public class AssignmentSubmissionMapper {

    public AssignmentSubmission toEntity(AssignmentSubmitRequest request) {
        return AssignmentSubmission.builder()
                .studentId(request.getStudentId())
                .courseId(request.getCourseId())
                .assignmentId(request.getAssignmentId())
                .answer(request.getAnswer())
                .checked(false)
                .marksObtained(0)
                .build();
    }

    public AssignmentSubmitResponse toResponse(AssignmentSubmission submission) {
        return AssignmentSubmitResponse.builder()
                .id(submission.getId())
                .studentId(submission.getStudentId())
                .courseId(submission.getCourseId())
                .assignmentId(submission.getAssignmentId())
                .answer(submission.getAnswer())
                .checked(submission.getChecked())
                .marksObtained(submission.getMarksObtained())
                .submittedAt(submission.getSubmittedAt())
                .updatedAt(submission.getUpdatedAt())
                .status(determineStatus(submission))
                .build();
    }

    private String determineStatus(AssignmentSubmission submission) {
        return submission.getChecked() ? "CHECKED" : "SUBMITTED";
    }
}
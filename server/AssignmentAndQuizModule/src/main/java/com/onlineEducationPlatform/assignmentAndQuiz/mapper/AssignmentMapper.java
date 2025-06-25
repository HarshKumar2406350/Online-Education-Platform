package com.onlineEducationPlatform.assignmentAndQuiz.mapper;

import com.onlineEducationPlatform.assignmentAndQuiz.dto.request.AssignmentRequest;
import com.onlineEducationPlatform.assignmentAndQuiz.dto.response.AssignmentResponse;
import com.onlineEducationPlatform.assignmentAndQuiz.entity.Assignment;
import org.springframework.stereotype.Component;

@Component
public class AssignmentMapper {

    public Assignment toEntity(AssignmentRequest request) {
        return Assignment.builder()
                .courseId(request.getCourseId())
                .title(request.getTitle())
                .description(request.getDescription())
                .totalMarks(request.getTotalMarks())
                .dueDate(request.getDueDate())
                .sequenceNo(request.getSequenceNo())
                .build();
    }

    public Assignment updateEntity(Assignment assignment, AssignmentRequest request) {
        assignment.setCourseId(request.getCourseId());
        assignment.setTitle(request.getTitle());
        assignment.setDescription(request.getDescription());
        assignment.setTotalMarks(request.getTotalMarks());
        assignment.setDueDate(request.getDueDate());
        assignment.setSequenceNo(request.getSequenceNo());
        return assignment;
    }

    public AssignmentResponse toResponse(Assignment assignment) {
        return AssignmentResponse.builder()
                .id(assignment.getId())
                .courseId(assignment.getCourseId())
                .title(assignment.getTitle())
                .description(assignment.getDescription())
                .totalMarks(assignment.getTotalMarks())
                .dueDate(assignment.getDueDate())
                .sequenceNo(assignment.getSequenceNo())
                .createdAt(assignment.getCreatedAt())
                .updatedAt(assignment.getUpdatedAt())
                .build();
    }
}
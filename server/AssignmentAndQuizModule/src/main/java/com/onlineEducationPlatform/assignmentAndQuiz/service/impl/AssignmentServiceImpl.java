package com.onlineEducationPlatform.assignmentAndQuiz.service.impl;

import com.onlineEducationPlatform.assignmentAndQuiz.dto.request.AssignmentRequest;
import com.onlineEducationPlatform.assignmentAndQuiz.dto.response.AssignmentResponse;
import com.onlineEducationPlatform.assignmentAndQuiz.entity.Assignment;
import com.onlineEducationPlatform.assignmentAndQuiz.exception.AssignmentNotFoundException;
import com.onlineEducationPlatform.assignmentAndQuiz.exception.UnauthorizedAccessException;
import com.onlineEducationPlatform.assignmentAndQuiz.repository.AssignmentRepository;
import com.onlineEducationPlatform.assignmentAndQuiz.service.AssignmentService;
import com.onlineEducationPlatform.assignmentAndQuiz.service.CourseValidationService;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {

    private static final Logger logger = LoggerFactory.getLogger(AssignmentServiceImpl.class);

    @Autowired
    private final AssignmentRepository assignmentRepository;

    @Autowired
    private final CourseValidationService courseValidationService;


    @Override
    @Transactional
    public AssignmentResponse createAssignment(AssignmentRequest request, String instructorId) {
        validateInstructorAccess(instructorId);
        validateCourseAccess(request.getCourseId(), instructorId);

        Assignment assignment = Assignment.builder()
                .courseId(request.getCourseId())
                .title(request.getTitle())
                .description(request.getDescription())
                .totalMarks(request.getTotalMarks())
                .dueDate(request.getDueDate())
                .sequenceNo(request.getSequenceNo())
                .build();

        return mapToResponse(assignmentRepository.save(assignment));
    }

    @Override
    @Transactional
    public AssignmentResponse updateAssignment(String id, AssignmentRequest request, String instructorId) {

        Assignment assignment = findAssignmentById(id);

        validateInstructorAccess(instructorId);
        validateCourseAccess(assignment.getCourseId(), instructorId);

        assignment.setTitle(request.getTitle());
        assignment.setDescription(request.getDescription());
        assignment.setTotalMarks(request.getTotalMarks());
        assignment.setDueDate(request.getDueDate());
        assignment.setSequenceNo(request.getSequenceNo());

        return mapToResponse(assignmentRepository.save(assignment));
    }

    @Override
    public AssignmentResponse getAssignment(String id, String userId) {
        Assignment assignment = findAssignmentById(id);

        logger.info("Assignment retrieved successfully");

//        validateUserAccess(assignment.getCourseId(), userId);
        return mapToResponse(assignment);
    }

    @Override
    public List<AssignmentResponse> getAssignmentsByCourse(String courseId, String userId) {
//        validateUserAccess(courseId, userId);
        return assignmentRepository.findByCourseIdOrderBySequenceNoAsc(courseId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteAssignment(String id, String instructorId) {
        Assignment assignment = findAssignmentById(id);
        validateInstructorAccess(instructorId);
        validateCourseAccess(assignment.getCourseId(), instructorId);
        assignmentRepository.delete(assignment);
    }

    private Assignment findAssignmentById(String id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new AssignmentNotFoundException(id));
    }

    private void validateInstructorAccess(String instructorId) {
        if (!hasRole("ROLE_INSTRUCTOR")) {
            throw new UnauthorizedAccessException("Only instructors can perform this action");
        }
    }

    private void validateCourseAccess(String courseId, String userId) {
        if (!courseValidationService.validateCourseAccess(courseId, userId)) {
            throw new UnauthorizedAccessException("User " + userId + " does not have access to course " + courseId);
        }
    }

    private void validateUserAccess(String courseId, String userId) {
        if (!courseValidationService.validateUserEnrollment(courseId, userId)) {
            throw new UnauthorizedAccessException("User " + userId + " is not enrolled in course " + courseId);
        }
    }

    private boolean hasRole(String role) {
        return SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(role));
    }

    private AssignmentResponse mapToResponse(Assignment assignment) {
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

    @Override
    @Transactional
    public void adminDeleteAssignment(String id, String courseId, String creatorId) {
        Assignment assignment = findAssignmentById(id);
        
        // Validate assignment belongs to specified course
        if (!assignment.getCourseId().equals(courseId)) {
            throw new UnauthorizedAccessException("Assignment does not belong to specified course");
        }

        // Verify assignment was created by specified creator
        validateCourseAccess(courseId, creatorId);

        assignmentRepository.delete(assignment);
    }

}
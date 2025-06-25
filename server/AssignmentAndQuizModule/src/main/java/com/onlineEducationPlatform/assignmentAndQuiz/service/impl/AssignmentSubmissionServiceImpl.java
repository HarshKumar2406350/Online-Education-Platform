package com.onlineEducationPlatform.assignmentAndQuiz.service.impl;

import com.onlineEducationPlatform.assignmentAndQuiz.dto.request.AssignmentSubmitRequest;
import com.onlineEducationPlatform.assignmentAndQuiz.dto.response.AssignmentSubmitResponse;
import com.onlineEducationPlatform.assignmentAndQuiz.entity.Assignment;
import com.onlineEducationPlatform.assignmentAndQuiz.entity.AssignmentSubmission;
import com.onlineEducationPlatform.assignmentAndQuiz.exception.AssignmentNotFoundException;
import com.onlineEducationPlatform.assignmentAndQuiz.exception.UnauthorizedAccessException;
import com.onlineEducationPlatform.assignmentAndQuiz.mapper.AssignmentSubmissionMapper;
import com.onlineEducationPlatform.assignmentAndQuiz.repository.AssignmentRepository;
import com.onlineEducationPlatform.assignmentAndQuiz.repository.AssignmentSubmissionRepository;
import com.onlineEducationPlatform.assignmentAndQuiz.service.AssignmentSubmissionService;
import com.onlineEducationPlatform.assignmentAndQuiz.service.CourseValidationService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssignmentSubmissionServiceImpl implements AssignmentSubmissionService {

    private final AssignmentRepository assignmentRepository;
    private final AssignmentSubmissionRepository submissionRepository;
    private final CourseValidationService courseValidationService;
    private final AssignmentSubmissionMapper submissionMapper;

    @Override
    @Transactional
    public AssignmentSubmitResponse submitAssignment(AssignmentSubmitRequest request) {
        Assignment assignment = findAssignmentById(request.getAssignmentId());
        validateSubmissionDeadline(assignment.getDueDate());
        validateStudentAccess(request.getStudentId());

        AssignmentSubmission submission = AssignmentSubmission.builder()
                .studentId(request.getStudentId())
                .courseId(request.getCourseId())
                .assignmentId(request.getAssignmentId())
                .answer(request.getAnswer())
                .checked(false)
                .marksObtained(0)
                .build();

        return mapToSubmissionResponse(submissionRepository.save(submission));
    }

    @Override
    @Transactional
    public AssignmentSubmitResponse gradeAssignment(String submissionId, Integer marks, String instructorId) {
        validateInstructorAccess(instructorId);
        AssignmentSubmission submission = findSubmissionById(submissionId);
        validateCourseAccess(submission.getCourseId(), instructorId);
        validateMarks(marks, submission.getAssignmentId());

        submission.setMarksObtained(marks);
        submission.setChecked(true);

        return mapToSubmissionResponse(submissionRepository.save(submission));
    }

    @Override
    @Transactional
    public AssignmentSubmitResponse updateAssignmentSubmission(@PathVariable String submissionId, AssignmentSubmitRequest request) {
        AssignmentSubmission  submission = submissionRepository.findById(submissionId)
            .orElseThrow(() -> new RuntimeException("Submission not found"));
        if (!submission.getStudentId().equals(request.getStudentId())) {
            throw new UnauthorizedAccessException("Student can only update their own submissions");
        }

        submission.setAnswer(request.getAnswer());
        return mapToSubmissionResponse(submissionRepository.save(submission));
    }

    @Override
    public List<AssignmentSubmitResponse> getSubmissionsByCourse(String courseId, String instructorId) {
        validateInstructorAccess(instructorId);
        validateCourseAccess(courseId, instructorId);
        
        return submissionRepository.findByCourseId(courseId)
                .stream()
                .map(this::mapToSubmissionResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AssignmentSubmitResponse> getSubmissionsByStudent(String studentId, String courseId) {
        validateStudentAccess(studentId);
        
        return submissionRepository.findByStudentIdAndCourseId(studentId, courseId)
                .stream()
                .map(this::mapToSubmissionResponse)
                .collect(Collectors.toList());
    }

    
    @Override
    @Transactional
    public void deleteSubmission(String submissionId) {
        AssignmentSubmission submission = submissionRepository.findById(submissionId)
            .orElseThrow(() -> new RuntimeException("Submission not found"));
        submissionRepository.delete(submission);
    }

    @Override
    public List<AssignmentSubmitResponse> getAllSubmissions() {
        return submissionRepository.findAll()
            .stream()
            .map(submissionMapper::toResponse)
            .collect(Collectors.toList());
    }

    private Assignment findAssignmentById(String id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new AssignmentNotFoundException(id));
    }

    private AssignmentSubmission findSubmissionById(String id) {
        return submissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Submission not found"));
    }

    private void validateSubmissionDeadline(LocalDateTime dueDate) {
        if (LocalDateTime.now().isAfter(dueDate)) {
            throw new RuntimeException("Assignment submission deadline has passed");
        }
    }

    private void validateMarks(Integer marks, String assignmentId) {
        Assignment assignment = findAssignmentById(assignmentId);
        if (marks > assignment.getTotalMarks()) {
            throw new RuntimeException("Marks cannot exceed total marks");
        }
    }

    private void validateInstructorAccess(String instructorId) {
        if (!hasRole("ROLE_INSTRUCTOR")) {
            throw new UnauthorizedAccessException("Only instructors can perform this action");
        }
    }

    private void validateStudentAccess(String studentId) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!currentUser.equals(studentId) && !hasRole("ROLE_ADMIN")) {
            throw new UnauthorizedAccessException("Unauthorized access to student submission");
        }
    }


    private void validateCourseAccess(String courseId, String userId) {
        if (!courseValidationService.validateCourseAccess(courseId, userId)) {
            throw new UnauthorizedAccessException("User " + userId + " does not have access to course " + courseId);
        }
    }

    private boolean hasRole(String role) {
        return SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(role));
    }

    private AssignmentSubmitResponse mapToSubmissionResponse(AssignmentSubmission submission) {
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
                .status(determineSubmissionStatus(submission))
                .build();
    }

    private String determineSubmissionStatus(AssignmentSubmission submission) {
        return submission.getChecked() ? "CHECKED" : "SUBMITTED";
    }
}
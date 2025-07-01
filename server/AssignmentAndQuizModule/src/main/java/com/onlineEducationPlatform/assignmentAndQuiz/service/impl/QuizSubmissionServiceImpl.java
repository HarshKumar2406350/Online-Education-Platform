package com.onlineEducationPlatform.assignmentAndQuiz.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.onlineEducationPlatform.assignmentAndQuiz.dto.request.QuizSubmitRequest;
import com.onlineEducationPlatform.assignmentAndQuiz.dto.response.QuizSubmitResponse;
import com.onlineEducationPlatform.assignmentAndQuiz.entity.Quiz;
import com.onlineEducationPlatform.assignmentAndQuiz.entity.QuizSubmission;
import com.onlineEducationPlatform.assignmentAndQuiz.exception.QuizNotFoundException;
import com.onlineEducationPlatform.assignmentAndQuiz.exception.UnauthorizedAccessException;
import com.onlineEducationPlatform.assignmentAndQuiz.mapper.QuizSubmissionMapper;
import com.onlineEducationPlatform.assignmentAndQuiz.repository.QuizRepository;
import com.onlineEducationPlatform.assignmentAndQuiz.repository.QuizSubmissionRepository;
import com.onlineEducationPlatform.assignmentAndQuiz.service.CourseValidationService;
import com.onlineEducationPlatform.assignmentAndQuiz.service.QuizSubmissionService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class
QuizSubmissionServiceImpl implements QuizSubmissionService {

    private final QuizRepository quizRepository;
    private final QuizSubmissionRepository submissionRepository;
    private final CourseValidationService courseValidationService;
    private final QuizSubmissionMapper submissionMapper;

    @Override
    @Transactional
    public QuizSubmitResponse submitQuiz(QuizSubmitRequest request) {
        Quiz quiz = quizRepository.findById(request.getQuizId())
                .orElseThrow(() -> new QuizNotFoundException(request.getQuizId()));

        validateNotAlreadySubmitted(request.getStudentId(), request.getQuizId());

        boolean isCorrect = quiz.getCorrectAnswer().equals(request.getAnswerOption());
        


        QuizSubmission submission = submissionMapper.toEntity(request, isCorrect);
        

        return mapToSubmissionResponse(submissionRepository.save(submission));
    }

    @Override
    @Transactional
    public void deleteSubmission(String submissionId) {
        QuizSubmission submission = submissionRepository.findById(submissionId)
            .orElseThrow(() -> new RuntimeException("Quiz submission not found"));
        submissionRepository.delete(submission);
    }

    @Override
    @Transactional
    public QuizSubmitResponse updateAnswer(String submissionId, QuizSubmitRequest request, String studentId) {
        // Find existing submission
        QuizSubmission submission = submissionRepository.findById(submissionId)
            .orElseThrow(() -> new RuntimeException("Quiz submission not found"));

        // Validate student owns the submission
        if (!submission.getStudentId().equals(studentId)) {
            throw new UnauthorizedAccessException("Student can only update their own submissions");
        }

        // Get quiz to check correct answer
        Quiz quiz = quizRepository.findById(submission.getQuizId())
            .orElseThrow(() -> new QuizNotFoundException(submission.getQuizId()));

        // Update submission
        boolean isCorrect = quiz.getCorrectAnswer().equals(request.getAnswerOption());

        submission.setAnswerOption(request.getAnswerOption());

        submission.setMarksObtained(isCorrect ? 1 : 0);

        return submissionMapper.toResponse(submissionRepository.save(submission));
    }

    @Override
    public List<QuizSubmitResponse> getSubmissionsByCourse(String courseId, String instructorId) {
        validateInstructorAccess(instructorId);
        return submissionRepository.findByCourseId(courseId)
                .stream()
                .map(this::mapToSubmissionResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<QuizSubmitResponse> getSubmissionsByStudent(String studentId, String courseId) {
        return submissionRepository.findByStudentIdAndCourseId(studentId, courseId)
                .stream()
                .map(this::mapToSubmissionResponse)
                .collect(Collectors.toList());
    }

    @Override
    public QuizSubmitResponse getSubmissionResult(String submissionId, String userId) {
        QuizSubmission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        validateSubmissionAccess(submission, userId);
        return mapToSubmissionResponse(submission);
    }

    @Override
    public List<QuizSubmitResponse> getPendingSubmissionsByCourse(String courseId, String instructorId) {
        validateInstructorAccess(instructorId);
        validateCourseAccess(courseId, instructorId);
        return submissionRepository.findByCourseIdAndStatus(courseId, QuizSubmission.SubmissionStatus.PENDING)
                .stream()
                .map(this::mapToSubmissionResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<QuizSubmitResponse> getCheckedSubmissionsByCourse(String courseId, String instructorId) {
        validateInstructorAccess(instructorId);
        validateCourseAccess(courseId, instructorId);
        return submissionRepository.findByCourseIdAndStatus(courseId, QuizSubmission.SubmissionStatus.CHECKED)
                .stream()
                .map(this::mapToSubmissionResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Integer> getMarksByStudentIdAndCourseIdAndQuizId(String studentId, String courseId, String quizId) {
        Optional<QuizSubmission> submission = submissionRepository.findByStudentIdAndCourseIdAndQuizId(studentId, courseId, quizId);
        if (submission.isPresent()) {
            return Optional.of(submission.get().getMarksObtained());
        }
        return Optional.empty();
    }


    private void validateNotAlreadySubmitted(String studentId, String quizId) {
        if (submissionRepository.existsByStudentIdAndQuizId(studentId, quizId)) {
            throw new RuntimeException("Student has already submitted this quiz");
        }
    }

    private void validateInstructorAccess(String userId) {
        if (!checkIfInstructor(userId)) {
            throw new UnauthorizedAccessException("Only instructors can access submissions");
        }
    }

    private boolean checkIfInstructor(String userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.getName().equals(userId)) {
            throw new UnauthorizedAccessException("Invalid user authentication");
        }
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_INSTRUCTOR"));
    }

    private void validateSubmissionAccess(QuizSubmission submission, String userId) {
        boolean isOwner = submission.getStudentId().equals(userId);
        boolean isInstructor = checkIfInstructor(userId);
        
        if (!isOwner && !isInstructor) {
            throw new UnauthorizedAccessException("User not authorized to view this submission");
        }

        // Validate course access for instructors
        if (isInstructor) {
            if (!courseValidationService.validateCourseAccess(submission.getCourseId(), userId)) {
                throw new UnauthorizedAccessException("Instructor does not have access to this course");
            }
        }
    }

    private QuizSubmitResponse mapToSubmissionResponse(QuizSubmission submission) {

        return submissionMapper.toResponse(submission);
    }

    private void validateCourseAccess(String courseId, String userId) {
        if (!courseValidationService.validateCourseAccess(courseId, userId)) {
            throw new UnauthorizedAccessException("User does not have access to this course");
        }
    }

}
package com.onlineEducationPlatform.assignmentAndQuiz.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.onlineEducationPlatform.assignmentAndQuiz.dto.request.QuizRequest;
import com.onlineEducationPlatform.assignmentAndQuiz.dto.response.QuizResponse;
import com.onlineEducationPlatform.assignmentAndQuiz.entity.Quiz;
import com.onlineEducationPlatform.assignmentAndQuiz.exception.QuizNotFoundException;
import com.onlineEducationPlatform.assignmentAndQuiz.exception.UnauthorizedAccessException;
import com.onlineEducationPlatform.assignmentAndQuiz.mapper.QuizMapper;
import com.onlineEducationPlatform.assignmentAndQuiz.repository.QuizRepository;
import com.onlineEducationPlatform.assignmentAndQuiz.service.CourseValidationService;
import com.onlineEducationPlatform.assignmentAndQuiz.service.QuizService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;
    private final QuizMapper quizMapper;
    private final CourseValidationService courseValidationService;


    @Override
    @Transactional
    public QuizResponse createQuiz(QuizRequest request, String instructorId) {
        validateInstructorAccess(request.getCourseId(), instructorId);

        Quiz quiz = Quiz.builder()
                .courseId(request.getCourseId())
                .question(request.getQuestion())
                .answerOptions(request.getAnswerOptions())
                .correctAnswer(request.getCorrectAnswer())
                .build();

        return mapToResponse(quizRepository.save(quiz), false);
    }

    @Override
    @Transactional
    public QuizResponse updateQuiz(String id, QuizRequest request, String instructorId) {
        Quiz quiz = findQuizById(id);
        validateInstructorAccess(quiz.getCourseId(), instructorId);

        quiz.setQuestion(request.getQuestion());
        quiz.setAnswerOptions(request.getAnswerOptions());
        quiz.setCorrectAnswer(request.getCorrectAnswer());

        return mapToResponse(quizRepository.save(quiz), true);
    }

    @Override
    public QuizResponse getQuiz(String id, String userId) {
        Quiz quiz = findQuizById(id);
        boolean isInstructor = checkIfInstructor(userId);
        return mapToResponse(quiz, isInstructor);
    }

    @Override
    public List<QuizResponse> getQuizzesByCourse(String courseId, String userId) {
        boolean isInstructor = checkIfInstructor(userId);
        return quizRepository.findByCourseId(courseId)
                .stream()
                .map(quiz -> mapToResponse(quiz, isInstructor))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteQuiz(String id, String instructorId) {
        Quiz quiz = findQuizById(id);
        validateInstructorAccess(quiz.getCourseId(), instructorId);
        quizRepository.delete(quiz);
    }

    private Quiz findQuizById(String id) {
        return quizRepository.findById(id)
                .orElseThrow(() -> new QuizNotFoundException(id));
    }




    @Override
    @Transactional
    public void adminDeleteQuiz(String id, String courseId, String creatorId) {
        Quiz quiz = findQuizById(id);
        
        // Validate quiz belongs to specified course
        if (!quiz.getCourseId().equals(courseId)) {
            throw new UnauthorizedAccessException("Quiz does not belong to specified course");
        }

        // Verify quiz was created by specified creator
        validateCourseAccess(courseId, creatorId);

        quizRepository.delete(quiz);
    }

    @Override
    @Transactional
    public List<QuizResponse> batchCreateQuiz(List<QuizRequest> requests, String instructorId) {
        // Validate instructor access
        validateInstructorAccess(instructorId);
        
        // Validate all quizzes belong to same course
        String courseId = requests.get(0).getCourseId();
        if (!requests.stream().allMatch(req -> req.getCourseId().equals(courseId))) {
            throw new IllegalArgumentException("All quizzes must belong to the same course");
        }

        // Validate course access
        validateCourseAccess(courseId, instructorId);

        return requests.stream()
            .map(request -> {
                Quiz quiz = quizMapper.toEntity(request);
                Quiz savedQuiz = quizRepository.save(quiz);
                return quizMapper.toResponse(savedQuiz, true);
            })
            .collect(Collectors.toList());
    }
    
    private boolean checkIfInstructor(String userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_INSTRUCTOR"));
    }

    private QuizResponse mapToResponse(Quiz quiz, boolean includeCorrectAnswer) {
        return QuizResponse.builder()
                .id(quiz.getId())
                .courseId(quiz.getCourseId())
                .question(quiz.getQuestion())
                .answerOptions(quiz.getAnswerOptions())
                .correctAnswer(includeCorrectAnswer ? quiz.getCorrectAnswer() : null)
                .createdAt(quiz.getCreatedAt())
                .updatedAt(quiz.getUpdatedAt())
                .build();
    }
    

    private void validateCourseAccess(String courseId, String userId) {
        if (!courseValidationService.validateCourseAccess(courseId, userId)) {
            throw new UnauthorizedAccessException("User " + userId + " does not have access to course " + courseId);
        }
    }

    private void validateInstructorAccess(String courseId, String instructorId) {
        validateInstructorAccess(instructorId);
        validateCourseAccess(courseId, instructorId);
    }

    private void validateInstructorAccess(String instructorId) {
        if (!checkIfInstructor(instructorId)) {
            throw new UnauthorizedAccessException("Only instructors can perform this action");
        }
    }


    
}
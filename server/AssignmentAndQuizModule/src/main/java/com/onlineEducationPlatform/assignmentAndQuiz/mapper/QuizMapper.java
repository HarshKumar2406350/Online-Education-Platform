package com.onlineEducationPlatform.assignmentAndQuiz.mapper;

import com.onlineEducationPlatform.assignmentAndQuiz.dto.request.QuizRequest;
import com.onlineEducationPlatform.assignmentAndQuiz.dto.response.QuizResponse;
import com.onlineEducationPlatform.assignmentAndQuiz.entity.Quiz;
import org.springframework.stereotype.Component;

@Component
public class QuizMapper {

    public Quiz toEntity(QuizRequest request) {
        return Quiz.builder()
                .courseId(request.getCourseId())
                .question(request.getQuestion())
                .answerOptions(request.getAnswerOptions())
                .correctAnswer(request.getCorrectAnswer())
                .build();
    }

    public Quiz updateEntity(Quiz quiz, QuizRequest request) {
        quiz.setQuestion(request.getQuestion());
        quiz.setAnswerOptions(request.getAnswerOptions());
        quiz.setCorrectAnswer(request.getCorrectAnswer());
        return quiz;
    }

    public QuizResponse toResponse(Quiz quiz, boolean includeCorrectAnswer) {
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
}
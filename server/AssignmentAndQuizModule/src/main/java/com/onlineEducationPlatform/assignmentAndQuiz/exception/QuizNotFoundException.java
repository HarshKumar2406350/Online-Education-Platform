package com.onlineEducationPlatform.assignmentAndQuiz.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class QuizNotFoundException extends RuntimeException {
    public QuizNotFoundException(String quizId) {
        super("Quiz not found with id: " + quizId);
    }

    public QuizNotFoundException(String quizId, String courseId) {
        super("Quiz not found with id: " + quizId + " in course: " + courseId);
    }
}
package com.onlineEducationPlatform.assignmentAndQuiz.repository;

import com.onlineEducationPlatform.assignmentAndQuiz.entity.QuizSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface QuizSubmissionRepository extends JpaRepository<QuizSubmission, String> {
    
    // Find submission by student and quiz
    Optional<QuizSubmission> findByStudentIdAndQuizId(String studentId, String quizId);
    
    // Find all submissions for a course
    List<QuizSubmission> findByCourseId(String courseId);
    
    // Find all submissions by student
    List<QuizSubmission> findByStudentId(String studentId);
    
    // Find all submissions for a quiz
    List<QuizSubmission> findByQuizId(String quizId);

    // Find all submissions for a course with a specific status
    List<QuizSubmission> findByCourseIdAndStatus(String courseId, QuizSubmission.SubmissionStatus status);
    
    
    // Check if student has attempted quiz
    boolean existsByStudentIdAndQuizId(String studentId, String quizId);

    // Find all submissions by student and course
    List<QuizSubmission> findByStudentIdAndCourseId(String studentId, String courseId);
}
package com.onlineEducationPlatform.assignmentAndQuiz.repository;

import com.onlineEducationPlatform.assignmentAndQuiz.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, String> {
    
    // Find quizzes by course ID
    List<Quiz> findByCourseId(String courseId);
    
    // Find quiz by ID and course ID
    Optional<Quiz> findByIdAndCourseId(String id, String courseId);
    
    // Check if quiz exists for a course
    boolean existsByCourseIdAndId(String courseId, String id);
    
    // Count quizzes in a course
    long countByCourseId(String courseId);
}
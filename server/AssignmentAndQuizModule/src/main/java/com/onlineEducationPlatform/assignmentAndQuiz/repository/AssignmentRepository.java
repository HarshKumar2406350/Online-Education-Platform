package com.onlineEducationPlatform.assignmentAndQuiz.repository;

import com.onlineEducationPlatform.assignmentAndQuiz.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, String> {
    
    // Find assignments by course ID
    List<Assignment> findByCourseId(String courseId);
    
    // Find assignment by ID and course ID
    Optional<Assignment> findByIdAndCourseId(String id, String courseId);
    
    // Find assignments by course ID ordered by sequence number
    List<Assignment> findByCourseIdOrderBySequenceNoAsc(String courseId);
    
    // Check if assignment exists for a course
    boolean existsByCourseIdAndId(String courseId, String id);
}
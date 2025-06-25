package com.onlineEducationPlatform.assignmentAndQuiz.repository;

import com.onlineEducationPlatform.assignmentAndQuiz.entity.AssignmentSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AssignmentSubmissionRepository extends JpaRepository<AssignmentSubmission, String> {
    
    // Find submission by student and assignment
    Optional<AssignmentSubmission> findByStudentIdAndAssignmentId(String studentId, String assignmentId);
    
    // Find all submissions for a course
    List<AssignmentSubmission> findByCourseId(String courseId);
    
    // Find all submissions by student
    List<AssignmentSubmission> findByStudentId(String studentId);
    
    // Find all submissions for an assignment
    List<AssignmentSubmission> findByAssignmentId(String assignmentId);
    
    // Find unchecked submissions for a course
    List<AssignmentSubmission> findByCourseIdAndCheckedFalse(String courseId);
    
    // Check if student has submitted for an assignment
    boolean existsByStudentIdAndAssignmentId(String studentId, String assignmentId);

    List<AssignmentSubmission> findByStudentIdAndCourseId(String studentId, String courseId);

}
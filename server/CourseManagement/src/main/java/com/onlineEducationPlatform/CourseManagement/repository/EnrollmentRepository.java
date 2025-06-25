package com.onlineEducationPlatform.CourseManagement.repository;

import com.onlineEducationPlatform.CourseManagement.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, String> {
    
    // Find enrollments by student
    List<Enrollment> findByStudentId(String studentId);
    
    // Find enrollments by course
    List<Enrollment> findByCourseId(String courseId);
    
    // Check if student is enrolled in course
    boolean existsByCourseIdAndStudentId(String courseId, String studentId);
    
    // Delete enrollment by course and student
    void deleteByCourseIdAndStudentId(String courseId, String studentId);

    // Find enrollments by course and instructor
    List<Enrollment> findByCourseIdAndStudentId(String courseId, String instructorId);
}
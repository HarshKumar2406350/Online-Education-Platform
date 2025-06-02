package com.onlineEducationPlatform.CourseManagement.repository;


import com.onlineEducationPlatform.CourseManagement.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
    
    // Find courses by instructor
    List<Course> findByInstructorId(String instructorId);
    
    // Find courses by category
    List<Course> findByCategory(String category);
    
    // Check if course exists and belongs to instructor
    boolean existsByIdAndInstructorId(String courseId, String instructorId);
}
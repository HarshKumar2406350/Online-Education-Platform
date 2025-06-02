package com.onlineEducationPlatform.CourseManagement.service;

import com.onlineEducationPlatform.CourseManagement.dto.response.EnrollmentResponse;
import java.util.List;

public interface EnrollmentService {
    EnrollmentResponse enrollInCourse(String courseId, String studentId);
    void unenrollFromCourse(String courseId, String studentId);
    List<EnrollmentResponse> getEnrollmentsByStudent(String studentId);
    List<EnrollmentResponse> getEnrollmentsByCourse(String courseId, String instructorId);
    boolean isEnrolled(String courseId, String studentId);
}
package com.onlineEducationPlatform.CourseManagement.service;

import com.onlineEducationPlatform.CourseManagement.dto.request.CourseRequest;
import com.onlineEducationPlatform.CourseManagement.dto.response.CourseDeleteResponse;
import com.onlineEducationPlatform.CourseManagement.dto.response.CourseResponse;
import com.onlineEducationPlatform.CourseManagement.dto.response.ValidationResponse;

import java.util.List;

public interface CourseService {
    CourseResponse createCourse(CourseRequest request, String instructorId);

    CourseResponse updateCourse(String courseId, CourseRequest request, String instructorId);

    CourseResponse getCourseById(String courseId);

    List<CourseResponse> getAllCourses();

    List<CourseResponse> getCoursesByInstructor(String instructorId);

    CourseDeleteResponse deleteCourse(String courseId, String instructorId);

    ValidationResponse ValidateCourseOwnership(String courseId, String instructorId);
}
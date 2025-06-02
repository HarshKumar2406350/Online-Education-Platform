package com.onlineEducationPlatform.CourseManagement.service.Impl;

import com.onlineEducationPlatform.CourseManagement.dto.request.CourseRequest;
import com.onlineEducationPlatform.CourseManagement.dto.response.CourseDeleteResponse;
import com.onlineEducationPlatform.CourseManagement.dto.response.CourseResponse;
import com.onlineEducationPlatform.CourseManagement.entity.Course;
import com.onlineEducationPlatform.CourseManagement.exception.CourseNotFoundException;
import com.onlineEducationPlatform.CourseManagement.exception.UnauthorizedAccessException;
import com.onlineEducationPlatform.CourseManagement.mapper.CourseMapper;
import com.onlineEducationPlatform.CourseManagement.repository.CourseRepository;
import com.onlineEducationPlatform.CourseManagement.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Override
    @Transactional
    public CourseResponse createCourse(CourseRequest request, String instructorId) {
        Course course = courseMapper.toEntity(request, instructorId);
        Course savedCourse = courseRepository.save(course);
        return courseMapper.toResponse(savedCourse);
    }

    @Override
    @Transactional
    public CourseResponse updateCourse(String courseId, CourseRequest request, String instructorId) {
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new CourseNotFoundException(courseId));

        if (!course.getInstructorId().equals(instructorId)) {
            throw new UnauthorizedAccessException("Not authorized to update this course");
        }

        courseMapper.updateEntity(course, request);
        Course updatedCourse = courseRepository.save(course);
        return courseMapper.toResponse(updatedCourse);
    }

    @Override
    @Transactional
    public CourseDeleteResponse deleteCourse(String courseId, String instructorId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with ID: " + courseId));
                
        if (!course.getInstructorId().equals(instructorId)) {
            throw new UnauthorizedAccessException("You are not authorized to delete this course");
        }
        
        CourseResponse courseResponse = courseMapper.toResponse(course);
        courseRepository.delete(course);
        
        return CourseDeleteResponse.builder()
                .message("Course Deleted successfully")
                .course(courseResponse)
                .build();
    }

    @Override
    public CourseResponse getCourseById(String courseId) {
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new CourseNotFoundException(courseId));
        return courseMapper.toResponse(course);
    }

    @Override
    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll().stream()
            .map(courseMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<CourseResponse> getCoursesByInstructor(String instructorId) {
        return courseRepository.findByInstructorId(instructorId).stream()
            .map(courseMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public boolean isInstructorOfCourse(String courseId, String instructorId) {
        return courseRepository.existsByIdAndInstructorId(courseId, instructorId);
    }
}
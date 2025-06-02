package com.onlineEducationPlatform.CourseManagement.service.Impl;

import com.onlineEducationPlatform.CourseManagement.dto.response.EnrollmentResponse;
import com.onlineEducationPlatform.CourseManagement.entity.Enrollment;
import com.onlineEducationPlatform.CourseManagement.exception.CourseNotFoundException;
import com.onlineEducationPlatform.CourseManagement.exception.DuplicateEnrollmentException;
import com.onlineEducationPlatform.CourseManagement.exception.UnauthorizedAccessException;
import com.onlineEducationPlatform.CourseManagement.mapper.EnrollmentMapper;
import com.onlineEducationPlatform.CourseManagement.repository.CourseRepository;
import com.onlineEducationPlatform.CourseManagement.repository.EnrollmentRepository;
import com.onlineEducationPlatform.CourseManagement.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentMapper enrollmentMapper;

    @Override
    @Transactional
    public EnrollmentResponse enrollInCourse(String courseId, String studentId) {
        if (!courseRepository.existsById(courseId)) {
            throw new CourseNotFoundException(courseId);
        }

        if (enrollmentRepository.existsByCourseIdAndStudentId(courseId, studentId)) {
            throw new DuplicateEnrollmentException(studentId, courseId);
        }

        Enrollment enrollment = enrollmentMapper.createEnrollment(courseId, studentId);
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        return enrollmentMapper.toResponse(savedEnrollment);
    }

    @Override
    @Transactional
    public void unenrollFromCourse(String courseId, String studentId) {
        if (!enrollmentRepository.existsByCourseIdAndStudentId(courseId, studentId)) {
            throw new CourseNotFoundException("No enrollment found for course: " + courseId);
        }
        enrollmentRepository.deleteByCourseIdAndStudentId(courseId, studentId);
    }

    @Override
    public List<EnrollmentResponse> getEnrollmentsByStudent(String studentId) {
        return enrollmentRepository.findByStudentId(studentId).stream()
            .map(enrollmentMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<EnrollmentResponse> getEnrollmentsByCourse(String courseId, String instructorId) {
        if (!courseRepository.existsByIdAndInstructorId(courseId, instructorId)) {
            throw new UnauthorizedAccessException("Not authorized to view these enrollments");
        }
        return enrollmentRepository.findByCourseId(courseId).stream()
            .map(enrollmentMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public boolean isEnrolled(String courseId, String studentId) {
        return enrollmentRepository.existsByCourseIdAndStudentId(courseId, studentId);
    }
}
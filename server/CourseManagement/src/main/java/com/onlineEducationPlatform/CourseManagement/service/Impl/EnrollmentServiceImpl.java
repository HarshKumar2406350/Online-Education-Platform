package com.onlineEducationPlatform.CourseManagement.service.Impl;

import com.onlineEducationPlatform.CourseManagement.dto.response.EnrollmentResponse;
import com.onlineEducationPlatform.CourseManagement.dto.response.ValidationResponse;
import com.onlineEducationPlatform.CourseManagement.entity.Course;
import com.onlineEducationPlatform.CourseManagement.entity.Enrollment;
import com.onlineEducationPlatform.CourseManagement.exception.CourseNotFoundException;
import com.onlineEducationPlatform.CourseManagement.exception.DuplicateEnrollmentException;
import com.onlineEducationPlatform.CourseManagement.exception.UnauthorizedAccessException;
import com.onlineEducationPlatform.CourseManagement.mapper.CourseMapper;
import com.onlineEducationPlatform.CourseManagement.mapper.EnrollmentMapper;
import com.onlineEducationPlatform.CourseManagement.repository.CourseRepository;
import com.onlineEducationPlatform.CourseManagement.repository.EnrollmentRepository;
import com.onlineEducationPlatform.CourseManagement.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {
    @Autowired
    private final EnrollmentRepository enrollmentRepository;
    @Autowired
    private final CourseRepository courseRepository;
    @Autowired
    private final EnrollmentMapper enrollmentMapper;
    @Autowired
    private final CourseMapper courseMapper;

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
    public ValidationResponse isEnrolled(String courseId, String studentId) {
        //check if student is enrolled in the course
        if (enrollmentRepository.existsByCourseIdAndStudentId(courseId, studentId)) {
            Course course = courseRepository.findById(courseId).orElseThrow(
                    () -> new CourseNotFoundException(courseId)
            );
            Enrollment enrollment = enrollmentRepository.findByCourseIdAndStudentId(courseId, studentId).getFirst();
            return new ValidationResponse(true,
                    courseMapper.toResponse(course),
                    enrollmentMapper.toResponse(enrollment));

        }else{
            return new ValidationResponse(false, null, null);
        }
        //if yes then get enrollment info and course info and create ValidationResponse
        //if no return false with empty course info and empty enrollment info and forming validationResponse
    }
}
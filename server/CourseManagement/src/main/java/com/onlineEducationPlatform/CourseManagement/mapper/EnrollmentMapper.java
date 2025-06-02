package com.onlineEducationPlatform.CourseManagement.mapper;



import com.onlineEducationPlatform.CourseManagement.dto.response.EnrollmentResponse;
import com.onlineEducationPlatform.CourseManagement.entity.Enrollment;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EnrollmentMapper {
    
    private final CourseMapper courseMapper;
    
    public Enrollment createEnrollment(String courseId, String studentId) {
        return Enrollment.builder()
            .courseId(courseId)
            .studentId(studentId)
            .build();
    }
    
    public EnrollmentResponse toResponse(Enrollment enrollment) {
        EnrollmentResponse response = new EnrollmentResponse();
        response.setId(enrollment.getId());
        response.setCourseId(enrollment.getCourseId());
        response.setStudentId(enrollment.getStudentId());
        response.setEnrollmentDate(enrollment.getEnrollmentDate());
        
        if (enrollment.getCourse() != null) {
            response.setCourse(courseMapper.toResponse(enrollment.getCourse()));
        }
        
        return response;
    }
}
package com.onlineEducationPlatform.CourseManagement.entity;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "course_enrollments", 
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"course_id", "student_id"},
            name = "uk_course_student"
        )
    }
)
public class Enrollment {
    
    @Id
    @GeneratedValue(generator = "enrollment-id")
    @GenericGenerator(
        name = "enrollment-id",
        type = com.onlineEducationPlatform.CourseManagement.util.EnrollmentIdGenerator.class
    )
    @Column(name = "enrollment_id", length = 7, nullable = false)
    private String id;

    @Column(name = "course_id", length = 50, nullable = false)
    private String courseId;

    @Column(name = "student_id", length = 50, nullable = false)
    private String studentId;

    @Column(name = "enrollment_date", nullable = false)
    private LocalDateTime enrollmentDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", referencedColumnName = "course_id", 
                insertable = false, updatable = false)
    private Course course;

    @PrePersist
    protected void onCreate() {
        enrollmentDate = LocalDateTime.now();
    }
}
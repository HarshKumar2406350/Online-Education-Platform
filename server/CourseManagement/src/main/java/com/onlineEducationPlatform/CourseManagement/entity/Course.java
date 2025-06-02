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
@Table(name = "courses")
public class Course {
    
    @Id
    @GeneratedValue(generator = "course-id")
    @GenericGenerator(
        name = "course-id",
        type = com.onlineEducationPlatform.CourseManagement.util.CourseIdGenerator.class
    )
    @Column(name = "course_id", length = 5, nullable = false)
    private String id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "instructor_id", length = 100, nullable = false)
    private String instructorId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private CourseCategory category;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Version
    @Column(columnDefinition = "BIGINT DEFAULT 0")
    private Long version;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

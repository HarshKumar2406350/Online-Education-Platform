package com.onlineEducationPlatform.assignmentAndQuiz.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import java.time.LocalDateTime;

@Entity
@Table(name = "assignments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Assignment {
    @Id
    @GeneratedValue(generator = "assignment-id")
    @GenericGenerator(name = "assignment-id", 
                     strategy = "com.onlineEducationPlatform.assignmentAndQuiz.util.AssignmentIdGenerator")
    private String id;  // 7-digit

    @NotBlank(message = "Course ID is required")
    private String courseId;  // 5-10 digits

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Total marks is required")
    @Min(value = 0, message = "Total marks must be positive")
    private Integer totalMarks;

    @NotNull(message = "Due date is required")
    private LocalDateTime dueDate;

    @NotNull(message = "Sequence number is required")
    private Integer sequenceNo;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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
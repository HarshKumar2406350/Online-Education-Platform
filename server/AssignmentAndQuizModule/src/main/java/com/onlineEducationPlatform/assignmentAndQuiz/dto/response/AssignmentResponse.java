package com.onlineEducationPlatform.assignmentAndQuiz.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentResponse {
    private String id;
    private String courseId;
    private String title;
    private String description;
    private Integer totalMarks;
    private LocalDateTime dueDate;
    private Integer sequenceNo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
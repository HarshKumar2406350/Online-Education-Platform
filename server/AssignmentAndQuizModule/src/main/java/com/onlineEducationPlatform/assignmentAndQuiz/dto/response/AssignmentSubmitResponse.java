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
public class AssignmentSubmitResponse {
    private String id;
    private String studentId;
    private String courseId;
    private String assignmentId;
    private String answer;
    private Boolean checked;
    private Integer marksObtained;
    private LocalDateTime submittedAt;
    private LocalDateTime updatedAt;
    private String status;  // SUBMITTED, CHECKED, LATE_SUBMISSION etc.
}
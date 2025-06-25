package com.onlineEducationPlatform.assignmentAndQuiz.service;

import com.onlineEducationPlatform.assignmentAndQuiz.dto.request.AssignmentSubmitRequest;
import com.onlineEducationPlatform.assignmentAndQuiz.dto.response.AssignmentSubmitResponse;
import java.util.List;

public interface AssignmentSubmissionService {
    
    AssignmentSubmitResponse submitAssignment(AssignmentSubmitRequest request);
    
    AssignmentSubmitResponse gradeAssignment(String submissionId, Integer marks, String instructorId);

    AssignmentSubmitResponse updateAssignmentSubmission(String submissionId, AssignmentSubmitRequest request);
    
    List<AssignmentSubmitResponse> getSubmissionsByCourse(String courseId, String instructorId);
    
    List<AssignmentSubmitResponse> getSubmissionsByStudent(String studentId, String courseId);

     /**
     * Admin endpoint to delete a submission
     * @param submissionId ID of the submission to delete
     */
    void deleteSubmission(String submissionId);

    /**
     * Get all submissions in the system (Admin only)
     * @return List of all submissions
     */
    List<AssignmentSubmitResponse> getAllSubmissions();


}
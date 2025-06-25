package com.onlineEducationPlatform.assignmentAndQuiz.service;

import com.onlineEducationPlatform.assignmentAndQuiz.dto.request.AssignmentRequest;
import com.onlineEducationPlatform.assignmentAndQuiz.dto.response.AssignmentResponse;
import java.util.List;

public interface AssignmentService {
    
    AssignmentResponse createAssignment(AssignmentRequest request, String instructorId);
    
    AssignmentResponse updateAssignment(String id, AssignmentRequest request, String instructorId);
    
    AssignmentResponse getAssignment(String id, String userId);
    
    List<AssignmentResponse> getAssignmentsByCourse(String courseId, String userId);
    
    void deleteAssignment(String id, String instructorId);
    
     /**
     * Admin endpoint to delete an assignment
     * @param id Assignment ID
     * @param courseId Course ID
     * @param creatorId Original creator's ID
     */
    void adminDeleteAssignment(String id, String courseId, String creatorId);
}
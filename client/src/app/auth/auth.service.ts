import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserResponse } from '../dashboard/admin/manage-users/UserResponse';


interface AssignmentResponse {
  message: string;
  statusCode: number;
  body: any[];
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private userApiUrl = 'http://localhost:8080/api/userModule/api'; // Base URL for User Module
  private courseApiUrl = 'http://localhost:8081/api/CourseManagement/api/CourseManagement'; 
  // Base URL for Course Management Module
  private assignmentApiUrl='http://localhost:8082/api/AssignmentAndQuizManagement';
  

  constructor(private http: HttpClient) {}

  // Login method for User Module
  login(credentials: { email: string; password: string; role: string }): Observable<any> {
    return this.http.post(`${this.userApiUrl}/auth/login`, credentials, { withCredentials: true });
  }

  // Logout method for User Module
  logout(): Observable<any> {
    return this.http.post(`${this.userApiUrl}/auth/logout`, {}, { withCredentials: true });
  }

  // Get user profile method for User Module
  getUserProfile(): Observable<any> {
    const token = localStorage.getItem('token');
    return this.http.get(`${this.userApiUrl}/profile`, {
      headers: { Authorization: `Bearer ${token}` },
      withCredentials: true,
    });
  }

  // Register method for User Module
  register(userData: { name: string; email: string; password: string; role: string }): Observable<any> {
    return this.http.post(`${this.userApiUrl}/auth/register`, userData, { withCredentials: true });
  }

  // Create course method for Course Management Module
  createCourse(courseData: { title: string; description: string; category: string }): Observable<any> {
    const token = localStorage.getItem('token');
    return this.http.post(`${this.courseApiUrl}/courses`, courseData, {
      headers: { Authorization: `Bearer ${token}` },
      withCredentials: true,
    });
  }
  // Get instructor-specific courses method
  getInstructorCourses(): Observable<any[]> {
    const token = localStorage.getItem('token');
    return this.http.get<any[]>(`${this.courseApiUrl}/courses/instructor`, {
      headers: { Authorization: `Bearer ${token}` },
      withCredentials: true,
    });
  }

  // Get all courses method
  getAllCourses(): Observable<any[]> {
    const token = localStorage.getItem('token');
    return this.http.get<any[]>(`${this.courseApiUrl}/courses`, {
      headers: { Authorization: `Bearer ${token}` },
      withCredentials: true,
    });
  }

  // Enroll in a course method
  enrollInCourse(courseId: string): Observable<any> {
    const token = localStorage.getItem('token');
    return this.http.post(`${this.courseApiUrl}/enrollments/${courseId}/enroll`, {},{
      headers: { Authorization: `Bearer ${token}` },
      withCredentials: true,
    });
  }

  //Get course by Id
  getCourseDetails(courseId: string): Observable<any> {
    const token = localStorage.getItem('token');
    return this.http.get(`${this.courseApiUrl}/courses/${courseId}`, {
      headers: { Authorization: `Bearer ${token}` },
      withCredentials: true,
    });
  } 

  // Delete Course by Id
  deleteCourse(courseId: string): Observable<any> {
    const token = localStorage.getItem('token');
    return this.http.delete(`${this.courseApiUrl}/courses/${courseId}`, {
      headers: { Authorization: `Bearer ${token}` },
      withCredentials: true,
    });
  }

  //update course
  updateCourse(courseId: string, courseData: { title: string; description: string; category: string }): Observable<any> {
    const token = localStorage.getItem('token');
    return this.http.put(`${this.courseApiUrl}/courses/${courseId}`, courseData, {
      headers: { Authorization: `Bearer ${token}` },
      withCredentials: true,
    });
  }
  //get Enrollements for Instructor
  getEnrollments(courseId: string): Observable<any[]> {
    const token = localStorage.getItem('token');
    return this.http.get<any[]>(`${this.courseApiUrl}/enrollments/course/${courseId}`, {
      headers: { Authorization: `Bearer ${token}` },
      withCredentials: true,
    });
  }
  //Create Assignment for Instructor
  createAssignment(assignmentData: {
    courseId: string;
    title: string;
    description: string;
    totalMarks: number;
    dueDate: string;
    sequenceNo: number;
  }): Observable<any> {
    const token = localStorage.getItem('token');
    const headers = { Authorization: `Bearer ${token}` };
    console.log('Headers:', headers);
    console.log('Assignment Data:', assignmentData);
  
    return this.http.post(`${this.assignmentApiUrl}/assignments`, assignmentData, {
      headers,
      withCredentials: true,
    });
  }
  //Get Assignment By course
  getAssignmentsByCourse(courseId: string): Observable<AssignmentResponse> {
    const token = localStorage.getItem('token');
    return this.http.get<AssignmentResponse>(`${this.assignmentApiUrl}/assignments/course/${courseId}`, {
      headers: { Authorization: `Bearer ${token}` },
      withCredentials: true,
    });
  }
  //Delete Assignment by Course
  deleteAssignment(assignmentId: string): Observable<void> {
    const token = localStorage.getItem('token'); // Retrieve token from local storage
    return this.http.delete<void>(`${this.assignmentApiUrl}/assignments/${assignmentId}`, {
      headers: { Authorization: `Bearer ${token}` },
      withCredentials: true,
    });
  }

  //Create Quiz
  createQuiz(quizRequest: any): Observable<any> {
    const token = localStorage.getItem('token'); // Retrieve token from local storage
    return this.http.post<any>(`${this.assignmentApiUrl}/quizzes`, quizRequest, {
      headers: { Authorization: `Bearer ${token}` },
      withCredentials: true,
    });
  }

  //View  Qizes by CourseId
  getQuizzesByCourse(courseId: string): Observable<any[]> {
    const token = localStorage.getItem('token'); // Retrieve token from local storage
    return this.http.get<any[]>(`${this.assignmentApiUrl}/quizzes/course/${courseId}`, {
      headers: { Authorization: `Bearer ${token}` },
      withCredentials: true,
    });
  }
  //Delete Quiz by Id
  deleteQuiz(quizId: string): Observable<void> {
    const token = localStorage.getItem('token'); // Retrieve token from local storage
    return this.http.delete<void>(`${this.assignmentApiUrl}/quizzes/${quizId}`, {
      headers: { Authorization: `Bearer ${token}` },
      withCredentials: true,
    });
  }

  //Get Student's Enrolled Courses
  getStudentEnrollments(): Observable<any[]> {
    const token = localStorage.getItem('token'); // Retrieve token from local storage
    return this.http.get<any[]>(`${this.courseApiUrl}/enrollments/student`, {
      headers: { Authorization: `Bearer ${token}` },
      withCredentials: true,
    });
  }
  //UnEnroll From Course
  unenrollFromCourse(courseId: string): Observable<void> {
    const token = localStorage.getItem('token'); // Retrieve token from local storage
    return this.http.delete<void>(`${this.courseApiUrl}/enrollments/${courseId}/unenroll`, {
      headers: { Authorization: `Bearer ${token}` },
      withCredentials: true,
    });
  }
  
  //Submit Assignment
  submitAssignment(request: any): Observable<any> {
    const token = localStorage.getItem('token'); // Retrieve token from local storage
    return this.http.post<any>(`${this.assignmentApiUrl}/assignment-submissions`, request, {
      headers: { Authorization: `Bearer ${token}` },
      withCredentials: true,
    });
  }

    // Submit quiz answers
    submitQuiz(request: any): Observable<any> {
      const token = localStorage.getItem('token'); // Retrieve token from local storage
      return this.http.post<any>(`${this.assignmentApiUrl}/quiz-submissions`, request, {
        headers: { Authorization: `Bearer ${token}` },
        withCredentials: true,
      });
    }

      // Fetch details of a specific quiz
    getQuizDetails(quizId: string): Observable<any> {
      const token = localStorage.getItem('token'); // Retrieve token from local storage
      return this.http.get<any>(`${this.assignmentApiUrl}/quizzes/${quizId}`, {
        headers: { Authorization: `Bearer ${token}` },
        withCredentials: true,
      });
    }

  // Get progress for a specific course
  getSubmissionsByStudent(studentId: string, courseId: string): Observable<any> {
    const token = localStorage.getItem('token'); // Retrieve token from local storage
    return this.http.get<any>(`${this.assignmentApiUrl}/quiz-submissions/student/${studentId}/course/${courseId}`, {
      headers: { Authorization: `Bearer ${token}` },
      withCredentials: true,
    });
  }
  
  getSubmissionResult(submissionId: string): Observable<any> {
    const token = localStorage.getItem('token'); // Retrieve token from local storage
    return this.http.get<any>(`${this.assignmentApiUrl}/quiz-submissions/${submissionId}`, {
      headers: { Authorization: `Bearer ${token}` },
      withCredentials: true,
    });
  }

  // get marks by student and course and quiz
  getMarksByStudentIdAndCourseIdAndQuizId(studentId: string, courseId: string, quizId: string): Observable<number> {
    const token = localStorage.getItem('token'); // Retrieve token from local storage
    return this.http.get<number>(`${this.assignmentApiUrl}/quiz-submissions/student/${studentId}/course/${courseId}/quiz/${quizId}/marks`, {
      headers: { Authorization: `Bearer ${token}` },
      withCredentials: true,
    });
  }
  //get all users
  getAllUsers(): Observable<UserResponse> {
    const token = localStorage.getItem('token'); // Retrieve token from local storage
    return this.http.get<UserResponse>(`${this.userApiUrl}/all/users`, {
      headers: { Authorization: `Bearer ${token}` }, // Add Authorization header
      withCredentials: true, // Include credentials for cross-origin requests
    });
  }

  // Delete user by ID
  deleteUserById(userId: string): Observable<void> {
    const token = localStorage.getItem('token'); // Retrieve token from local storage
    return this.http.delete<void>(`${this.userApiUrl}/users/${userId}`, {
      headers: { Authorization: `Bearer ${token}` }, // Add Authorization header
      withCredentials: true, // Include credentials for cross-origin requests
    });
  }

    // Fetch submissions by course
    getSubmissionsByCourse(courseId: string): Observable<any[]> {
      const token = localStorage.getItem('token'); // Retrieve token from local storage
      console.log('Authorization Header:', { Authorization: `Bearer ${token}` });
      return this.http.get<any[]>(`${this.assignmentApiUrl}/assignment-submissions/course/${courseId}`, {
        headers: { Authorization: `Bearer ${token}` }, // Add Authorization header
        withCredentials: true, // Include credentials for cross-origin requests
      });
      
    }
    
    // Grade an assignment
    gradeAssignment(submissionId: string, marks: number): Observable<any> {
      const token = localStorage.getItem('token'); // Retrieve token from local storage
    
      return this.http.put<any>(
        `${this.assignmentApiUrl}/assignment-submissions/${submissionId}/grade?marks=${marks}`,
        {}, // Empty body as marks are passed in the query parameter
        {
          headers: { Authorization: `Bearer ${token}` }, // Add Authorization header
          withCredentials: true, // Include credentials for cross-origin requests
        }
      );
    }
 
}
# Assignment and Quiz Management Module

## Overview
This module is part of the Online Education Platform, handling all assignment and quiz-related operations. It provides RESTful APIs for managing assignments, quizzes, and their submissions.

## Technical Stack
- **Framework:** Spring Boot
- **Database:** MySQL
- **Security:** JWT-based authentication
- **Documentation:** Swagger/OpenAPI
- **Build Tool:** Maven

## Prerequisites
- JDK 17 or higher
- Maven 3.8+
- MySQL 8.0+
- Spring Boot 3.x

## Configuration
### Application Properties
```properties
spring.application.name=AssignmentAndQuizModule
server.port=8082
server.servlet.context-path=/api/AssignmentAndQuizManagment
```

## API Documentation

### Assignment Management

#### 1. Create Assignment
- **URL:** `/assignments`
- **Method:** POST
- **Auth Required:** Yes
- **Roles Allowed:** INSTRUCTOR, ADMIN
- **Request Body:**
```json
{
    "courseId": "string",
    "title": "string",
    "description": "string",
    "totalMarks": number,
    "dueDate": "yyyy-MM-dd'T'HH:mm:ss",
    "sequenceNo": number
}
```
- **Success Response:**
```json
{
    "message": "Assignment created successfully",
    "status": 201,
    "data": {
        "id": "7-digit-id",
        "courseId": "string",
        "title": "string",
        "description": "string",
        "totalMarks": number,
        "dueDate": "string",
        "sequenceNo": number,
        "createdAt": "timestamp",
        "updatedAt": "timestamp"
    }
}
```

#### 2. Update Assignment
- **URL:** `/assignments/{id}`
- **Method:** PUT
- **Auth Required:** Yes
- **Roles Allowed:** INSTRUCTOR (owner)
- **Request Body:** Same as Create Assignment
- **Success Response:** Similar to Create Assignment

#### 3. Get Assignment
- **URL:** `/assignments/{id}`
- **Method:** GET
- **Auth Required:** Yes
- **Roles Allowed:** INSTRUCTOR, STUDENT, ADMIN
- **Path Parameters:** 
  - id: Assignment ID
- **Success Response:**
```json
{
    "message": "Assignment fetched successfully",
    "status": 200,
    "data": {
        "id": "7-digit-id",
        "courseId": "string",
        "title": "string",
        "description": "string",
        "totalMarks": number,
        "dueDate": "string",
        "sequenceNo": number,
        "createdAt": "timestamp",
        "updatedAt": "timestamp"
    }
}
```

#### 4. Get Course Assignments
- **URL:** `/assignments/course/{courseId}`
- **Method:** GET
- **Auth Required:** Yes
- **Roles Allowed:** INSTRUCTOR, STUDENT, ADMIN
- **Path Parameters:**
  - courseId: Course ID
- **Success Response:**
```json
{
    "message": "Assignments fetched successfully",
    "status": 200,
    "data": [
        {
            "id": "7-digit-id",
            "courseId": "string",
            "title": "string",
            "description": "string",
            "totalMarks": number,
            "dueDate": "string",
            "sequenceNo": number
        }
    ]
}
```

#### 5. Delete Assignment
- **URL:** `/assignments/{id}`
- **Method:** DELETE
- **Auth Required:** Yes
- **Roles Allowed:** INSTRUCTOR (owner), ADMIN
- **Path Parameters:**
  - id: Assignment ID
- **Success Response:**
```json
{
    "message": "Assignment deleted successfully",
    "status": 200
}
```

### Assignment Submission Management

#### 1. Submit Assignment
- **URL:** `/assignment-submissions`
- **Method:** POST
- **Auth Required:** Yes
- **Roles Allowed:** STUDENT
- **Request Body:**
```json
{
    "assignmentId": "string",
    "studentId": "string",
    "courseId": "string",
    "answer": "string"
}
```

#### 2. Update Assignment Submission
- **URL:** `/assignment-submissions/{id}`
- **Method:** PUT
- **Auth Required:** Yes
- **Roles Allowed:** STUDENT (owner)
- **Request Body:**
```json
{
    "answer": "string"
}
```
- **Success Response:**
```json
{
    "message": "Submission updated successfully",
    "status": 200,
    "data": {
        "id": "8-digit-id",
        "assignmentId": "string",
        "answer": "string",
        "updatedAt": "timestamp"
    }
}
```

#### 3. Grade Assignment Submission
- **URL:** `/assignment-submissions/{id}/grade`
- **Method:** PUT
- **Auth Required:** Yes
- **Roles Allowed:** INSTRUCTOR
- **Request Body:**
```json
{
    "marksObtained": number,
    "checked": true
}
```
- **Success Response:**
```json
{
    "message": "Assignment graded successfully",
    "status": 200,
    "data": {
        "id": "8-digit-id",
        "marksObtained": number,
        "checked": true,
        "gradedAt": "timestamp"
    }
}
```

#### 4. Get Student Submissions
- **URL:** `/assignment-submissions/student/{studentId}/course/{courseId}`
- **Method:** GET
- **Auth Required:** Yes
- **Roles Allowed:** STUDENT (self), INSTRUCTOR, ADMIN
- **Success Response:**
```json
{
    "message": "Submissions fetched successfully",
    "status": 200,
    "data": [
        {
            "id": "8-digit-id",
            "assignmentId": "string",
            "answer": "string",
            "marksObtained": number,
            "checked": boolean,
            "submittedAt": "timestamp"
        }
    ]
}
```

### Quiz Management

#### 1. Create Quiz
- **URL:** `/quizzes`
- **Method:** POST
- **Auth Required:** Yes
- **Roles Allowed:** INSTRUCTOR, ADMIN
- **Request Body:**
```json
{
    "courseId": "string",
    "question": "string",
    "options": ["string", "string", "string", "string"],
    "correctAnswer": "string"
}
```
- **Success Response:**
```json
{
    "message": "Quiz created successfully",
    "status": 201,
    "data": {
        "id": "9-digit-id",
        "question": "string",
        "options": ["string"],
        "createdAt": "timestamp"
    }
}
```

#### 2. Update Quiz
- **URL:** `/quizzes/{id}`
- **Method:** PUT
- **Auth Required:** Yes
- **Roles Allowed:** INSTRUCTOR (owner)
- **Request Body:** Same as Create Quiz
- **Success Response:** Similar to Create Quiz

#### 3. Get Quiz
- **URL:** `/quizzes/{id}`
- **Method:** GET
- **Auth Required:** Yes
- **Roles Allowed:** INSTRUCTOR, STUDENT, ADMIN
- **Success Response:**
```json
{
    "message": "Quiz fetched successfully",
    "status": 200,
    "data": {
        "id": "9-digit-id",
        "question": "string",
        "options": ["string"],
        "courseId": "string"
    }
}
```

#### 4. Get Course Quizzes
- **URL:** `/quizzes/course/{courseId}`
- **Method:** GET
- **Auth Required:** Yes
- **Roles Allowed:** INSTRUCTOR, STUDENT, ADMIN
- **Success Response:**
```json
{
    "message": "Quizzes fetched successfully",
    "status": 200,
    "data": [
        {
            "id": "9-digit-id",
            "question": "string",
            "options": ["string"],
            "courseId": "string"
        }
    ]
}
```

### Quiz Submission Management

### Quiz Submission Management

#### 1. Submit Quiz
- **URL:** `/quiz-submissions`
- **Method:** POST
- **Auth Required:** Yes
- **Roles Allowed:** STUDENT
- **Request Body:**
```json
{
    "studentId": "string",
    "courseId": "string",
    "quizId": "string",
    "answerOption": "string"
}
```
- **Success Response:**
```json
{
    "message": "Quiz submitted successfully",
    "statusCode": 201,
    "body": {
        "id": "string",
        "studentId": "string",
        "courseId": "string",
        "quizId": "string",
        "answerOption": "string",
        "marksObtained": 0,
        "submittedAt": "datetime",
        "updatedAt": "datetime",
        "status": "PENDING"
    }
}
```

#### 2. Get All Submissions for Course
- **URL:** `/quiz-submissions/course/{courseId}`
- **Method:** GET
- **Auth Required:** Yes
- **Roles Allowed:** INSTRUCTOR
- **Path Parameters:** courseId
- **Success Response:**
```json
{
    "message": "Submissions retrieved successfully",
    "statusCode": 200,
    "body": [
        {
            "id": "string",
            "studentId": "string",
            "courseId": "string",
            "quizId": "string",
            "answerOption": "string",
            "marksObtained": number,
            "submittedAt": "datetime",
            "updatedAt": "datetime",
            "status": "string"
        }
    ]
}
```

#### 3. Get Student's Submissions
- **URL:** `/quiz-submissions/student/{studentId}/course/{courseId}`
- **Method:** GET
- **Auth Required:** Yes
- **Roles Allowed:** STUDENT (self), INSTRUCTOR
- **Path Parameters:** studentId, courseId
- **Success Response:** Same format as course submissions

#### 4. Get Submission Result
- **URL:** `/quiz-submissions/{submissionId}`
- **Method:** GET
- **Auth Required:** Yes
- **Roles Allowed:** STUDENT (owner), INSTRUCTOR
- **Path Parameters:** submissionId
- **Success Response:**
```json
{
    "message": "Submission retrieved successfully",
    "statusCode": 200,
    "body": {
        "id": "string",
        "studentId": "string",
        "courseId": "string",
        "quizId": "string",
        "answerOption": "string",
        "marksObtained": number,
        "submittedAt": "datetime",
        "updatedAt": "datetime",
        "status": "string"
    }
}
```

#### 5. Get Pending Submissions
- **URL:** `/quiz-submissions/course/{courseId}/pending`
- **Method:** GET
- **Auth Required:** Yes
- **Roles Allowed:** INSTRUCTOR
- **Path Parameters:** courseId
- **Success Response:** Same format as course submissions

#### 6. Get Checked Submissions
- **URL:** `/quiz-submissions/course/{courseId}/checked`
- **Method:** GET
- **Auth Required:** Yes
- **Roles Allowed:** INSTRUCTOR
- **Path Parameters:** courseId
- **Success Response:** Same format as course submissions

#### 7. Update Quiz Answer
- **URL:** `/quiz-submissions/{submissionId}/answer`
- **Method:** PUT
- **Auth Required:** Yes
- **Roles Allowed:** STUDENT (owner)
- **Path Parameters:** submissionId
- **Request Body:**
```json
{
    "studentId": "string",
    "courseId": "string",
    "quizId": "string",
    "answerOption": "string"
}
```
- **Success Response:** Same format as submit response

#### 8. Delete Submission
- **URL:** `/quiz-submissions/admin/{submissionId}`
- **Method:** DELETE
- **Auth Required:** Yes
- **Roles Allowed:** ADMIN
- **Path Parameters:** submissionId
- **Success Response:**
```json
{
    "message": "Quiz submission deleted successfully",
    "statusCode": 200,
    "body": null
}
```
## Error Responses
All endpoints may return the following error responses:

### 400 Bad Request
```json
{
    "message": "Invalid request parameters",
    "status": 400,
    "errors": ["Error details"]
}
```

### 401 Unauthorized
```json
{
    "message": "Authentication required",
    "status": 401
}
```

### 403 Forbidden
```json
{
    "message": "Access denied",
    "status": 403
}
```

### 404 Not Found
```json
{
    "message": "Resource not found",
    "status": 404
}
```

## Security
- JWT-based authentication
- Role-based access control
- Token expiration: 24 hours
- Cookie-based token storage

## Database Schema
### Assignment Table
- id (VARCHAR(7)) - Primary Key
- course_id (VARCHAR(10))
- title (VARCHAR(255))
- description (TEXT)
- total_marks (INT)
- due_date (TIMESTAMP)
- sequence_no (INT)
- created_at (TIMESTAMP)
- updated_at (TIMESTAMP)

### Quiz Table
- id (VARCHAR(9)) - Primary Key
- course_id (VARCHAR(10))
- question (TEXT)
- options (JSON)
- correct_answer (VARCHAR(255))
- created_at (TIMESTAMP)
- updated_at (TIMESTAMP)

## Running the Application
```bash
# Build the application
mvn clean install

# Run the application
mvn spring-boot:run
```

## Testing
```bash
# Run unit tests
mvn test

# Run integration tests
mvn verify
```

## Contributing
1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request

## License
This project is licensed under the MIT License - see the LICENSE file for details
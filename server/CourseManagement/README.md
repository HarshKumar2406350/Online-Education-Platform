# Course Management Module

## Overview
Course Management module for an online education platform that provides course creation, enrollment, and management functionalities with JWT-based authentication.

## Database Schema

### Tables Structure
```sql
-- Courses Table
CREATE TABLE courses (
    course_id VARCHAR(6) PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    instructor_id VARCHAR(6) NOT NULL,
    category VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    version BIGINT DEFAULT 0,
    FOREIGN KEY (instructor_id) REFERENCES users(id)
);

-- Course Enrollments Table
CREATE TABLE course_enrollments (
    enrollment_id VARCHAR(6) PRIMARY KEY,
    course_id VARCHAR(6) NOT NULL,
    student_id VARCHAR(6) NOT NULL,
    enrollment_date TIMESTAMP NOT NULL,
    FOREIGN KEY (course_id) REFERENCES courses(course_id),
    FOREIGN KEY (student_id) REFERENCES users(id)
);
```

## Course Categories
- PRODUCTIVITY
- BUSINESS
- TECHNOLOGY
- FINANCIAL
- EDUCATION

## API Documentation

### Course Management APIs

#### Create Course
```http
POST /api/courses
Authorization: Bearer {jwt-token}
Role: ADMIN, INSTRUCTOR

Request:
{
    "title": "string",
    "description": "string",
    "category": "TECHNOLOGY"
}

Response: (201 Created)
{
    "message": "Course created successfully",
    "course": {
        "courseId": "123456",
        "title": "string",
        "description": "string",
        "instructorId": "string",
        "category": "TECHNOLOGY"
    }
}
```

#### List All Courses
```http
GET /api/courses
Role: ALL

Response: (200 OK)
{
    "courses": [
        {course-object}
    ]
}
```

#### Get Course Details
```http
GET /api/courses/{courseId}
Role: ALL

Response: (200 OK)
{
    "course": {course-object}
}
```

#### Update Course
```http
PUT /api/courses/{courseId}
Role: ADMIN, COURSE_INSTRUCTOR

Request:
{
    "title": "string",
    "description": "string",
    "category": "TECHNOLOGY"
}

Response: (200 OK)
{
    "message": "Course updated successfully"
}
```

#### Delete Course
```http
DELETE /api/courses/{courseId}
Role: ADMIN, COURSE_INSTRUCTOR

Response: (200 OK)
{
    "message": "Course removed successfully"
}
```

### Enrollment Management APIs

#### Enroll in Course
```http
POST /api/courses/{courseId}/enroll
Role: ALL

Response: (201 Created)
{
    "enrollmentId": "123456",
    "courseId": "string",
    "studentId": "string",
    "enrollmentDate": "timestamp"
}
```

#### Unenroll from Course
```http
DELETE /api/courses/unenroll/{courseId}
Role: ADMIN, ENROLLED_STUDENT

Response: (200 OK)
{
    "message": "Successfully unenrolled"
}
```

#### Get Enrolled Students
```http
GET /api/courses/enrolled_Instructor/{courseId}
Role: ADMIN, COURSE_INSTRUCTOR

Response: (200 OK)
{
    "students": [
        {user-object}
    ]
}
```

#### Get Student's Enrolled Courses
```http
GET /api/courses/enrolled_student/{userId}
Role: ALL

Response: (200 OK)
{
    "courses": [
        {course-object}
    ]
}
```

## Role-Based Access Control

| Endpoint | ADMIN | INSTRUCTOR | STUDENT |
|----------|-------|------------|---------|
| Create Course | ✓ | ✓ | ✗ |
| List Courses | ✓ | ✓ | ✓ |
| Course Details | ✓ | ✓ | ✓ |
| Update Course | ✓ | Own Course | ✗ |
| Delete Course | ✓ | Own Course | ✗ |
| Enroll | ✓ | ✓ | ✓ |
| Unenroll | ✓ | ✗ | Own Enrollment |
| View Enrolled Students | ✓ | Own Course | ✗ |
| View Enrolled Courses | ✓ | ✓ | Own Courses |

## Status Codes
- 200: Success
- 201: Created
- 400: Bad Request
- 401: Unauthorized
- 403: Forbidden
- 404: Not Found
- 409: Conflict
- 500: Server Error

## Security Implementation
- JWT authentication integration
- Role-based authorization
- Token validation with User Module

## Data Validation Rules
1. Course:
   - Title: 5-100 characters
   - Description: Max 1000 characters
   - Valid category enum
   - Valid instructor role

2. Enrollment:
   - One enrollment per student per course
   - Valid course and student IDs

## Setup Instructions
```bash
# Build
mvn clean install

# Run
mvn spring-boot:run
```

## Dependencies
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
</dependencies>
```

### Directory Structure
```
courseModule/
├── src/main/java/com/onlineEducationPlatform/CourseManagement/
│   ├── config/                           # Configuration classes
│   │   ├── SecurityConfig.java           # Security configuration
│   │   └── JwtAuthenticationFilter.java  # JWT validation filter
│   │
│   ├── controller/                       # REST Controllers
│   │   ├── CourseController.java        # Course CRUD operations
│   │   └── EnrollmentController.java    # Enrollment operations
│   │
│   ├── dto/                             # Data Transfer Objects
│   │   ├── request/
│   │   │   ├── CourseRequest.java      # Course creation/update request
│   │   │   └── EnrollmentRequest.java  # Enrollment request
│   │   └── response/
│   │       ├── CourseResponse.java     # Course response
│   │       └── EnrollmentResponse.java # Enrollment response
│   │
│   ├── entity/                          # Database Entities
│   │   ├── Course.java                 # Course entity
│   │   ├── CourseCategory.java         # Course category enum
│   │   └── Enrollment.java             # Enrollment entity
│   │
│   ├── exception/                       # Custom Exceptions
│   │   ├── CourseNotFoundException.java
│   │   ├── DuplicateEnrollmentException.java
│   │   ├── UnauthorizedAccessException.java
│   │   └── GlobalExceptionHandler.java
│   │
│   ├── mapper/                          # Object Mappers
│   │   ├── CourseMapper.java           # Course DTO-Entity conversion
│   │   └── EnrollmentMapper.java       # Enrollment DTO-Entity conversion
│   │
│   ├── repository/                      # Data Access Layer
│   │   ├── CourseRepository.java       # Course JPA repository
│   │   └── EnrollmentRepository.java   # Enrollment JPA repository
│   │
│   ├── service/                         # Business Logic
│   │   ├── CourseService.java          # Course service interface
│   │   ├── EnrollmentService.java      # Enrollment service interface
│   │   └── impl/
│   │       ├── CourseServiceImpl.java
│   │       └── EnrollmentServiceImpl.java
│   │
│   ├── util/                           # Utilities
│   │   ├── CustomIdGenerator.java      # Base ID generator
│   │   ├── CourseIdGenerator.java      # 5-digit ID generator
│   │   └── EnrollmentIdGenerator.java  # 7-digit ID generator
│   │
│   └── CourseManagementApplication.java # Main application class
│
├── src/main/resources/
│   ├── application.properties           # Application configuration
│   └── db/migration/                   # Flyway migrations
│       ├── V1__Create_Courses_Table.sql
│       └── V2__Create_Enrollments_Table.sql
│
└── src/test/java/.../courseModule/
    ├── controller/
    │   ├── CourseControllerTest.java
    │   └── EnrollmentControllerTest.java
    ├── service/
    │   ├── CourseServiceTest.java
    │   └── EnrollmentServiceTest.java
    └── repository/
        ├── CourseRepositoryTest.java
        └── EnrollmentRepositoryTest.java
```

Key Components:

1. **Configuration**: Security and JWT setup
2. **Controllers**: REST endpoints for courses and enrollments
3. **DTOs**: Separate request/response objects
4. **Entities**: Database models with relationships
5. **Exceptions**: Custom exception handling
6. **Mappers**: DTO-Entity conversions
7. **Repositories**: Data access layer
8. **Services**: Business logic implementation
9. **Utils**: ID generators and helpers
10. **Tests**: Unit and integration tests

Each component follows:
- Single Responsibility Principle
- Clean Architecture principles
- Proper separation of concerns
- Clear package structure
- Testable design

This structure provides:
- Easy maintenance
- Clear dependencies
- Modular design
- Scalability
- Testing support

Let me know if you need implementation details for any specific component.courseModule/
├── src/main/java/com/onlineEducationPlatform/CourseManagement/
│   ├── config/                           # Configuration classes
│   │   ├── SecurityConfig.java           # Security configuration
│   │   └── JwtAuthenticationFilter.java  # JWT validation filter
│   │
│   ├── controller/                       # REST Controllers
│   │   ├── CourseController.java        # Course CRUD operations
│   │   └── EnrollmentController.java    # Enrollment operations
│   │
│   ├── dto/                             # Data Transfer Objects
│   │   ├── request/
│   │   │   ├── CourseRequest.java      # Course creation/update request
│   │   │   └── EnrollmentRequest.java  # Enrollment request
│   │   └── response/
│   │       ├── CourseResponse.java     # Course response
│   │       └── EnrollmentResponse.java # Enrollment response
│   │
│   ├── entity/                          # Database Entities
│   │   ├── Course.java                 # Course entity
│   │   ├── CourseCategory.java         # Course category enum
│   │   └── Enrollment.java             # Enrollment entity
│   │
│   ├── exception/                       # Custom Exceptions
│   │   ├── CourseNotFoundException.java
│   │   ├── DuplicateEnrollmentException.java
│   │   ├── UnauthorizedAccessException.java
│   │   └── GlobalExceptionHandler.java
│   │
│   ├── mapper/                          # Object Mappers
│   │   ├── CourseMapper.java           # Course DTO-Entity conversion
│   │   └── EnrollmentMapper.java       # Enrollment DTO-Entity conversion
│   │
│   ├── repository/                      # Data Access Layer
│   │   ├── CourseRepository.java       # Course JPA repository
│   │   └── EnrollmentRepository.java   # Enrollment JPA repository
│   │
│   ├── service/                         # Business Logic
│   │   ├── CourseService.java          # Course service interface
│   │   ├── EnrollmentService.java      # Enrollment service interface
│   │   └── impl/
│   │       ├── CourseServiceImpl.java
│   │       └── EnrollmentServiceImpl.java
│   │
│   ├── util/                           # Utilities
│   │   ├── CustomIdGenerator.java      # Base ID generator
│   │   ├── CourseIdGenerator.java      # 5-digit ID generator
│   │   └── EnrollmentIdGenerator.java  # 7-digit ID generator
│   │
│   └── CourseManagementApplication.java # Main application class
│
├── src/main/resources/
│   ├── application.properties           # Application configuration
│   └── db/migration/                   # Flyway migrations
│       ├── V1__Create_Courses_Table.sql
│       └── V2__Create_Enrollments_Table.sql
│
└── src/test/java/.../courseModule/
    ├── controller/
    │   ├── CourseControllerTest.java
    │   └── EnrollmentControllerTest.java
    ├── service/
    │   ├── CourseServiceTest.java
    │   └── EnrollmentServiceTest.java
    └── repository/
        ├── CourseRepositoryTest.java
        └── EnrollmentRepositoryTest.java
```

Key Components:

1. **Configuration**: Security and JWT setup
2. **Controllers**: REST endpoints for courses and enrollments
3. **DTOs**: Separate request/response objects
4. **Entities**: Database models with relationships
5. **Exceptions**: Custom exception handling
6. **Mappers**: DTO-Entity conversions
7. **Repositories**: Data access layer
8. **Services**: Business logic implementation
9. **Utils**: ID generators and helpers
10. **Tests**: Unit and integration tests

Each component follows:
- Single Responsibility Principle
- Clean Architecture principles
- Proper separation of concerns
- Clear package structure
- Testable design

This structure provides:
- Easy maintenance
- Clear dependencies
- Modular design
- Scalability
- Testing support
```

## License
MIT License
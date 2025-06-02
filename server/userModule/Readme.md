# Online Education Platform - User Module

## Overview
A comprehensive user management module providing authentication, authorization, and profile management functionalities for an online education platform.

## Technology Stack
- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Database**: MySQL 8.0+
- **Security**: JWT + Spring Security
- **Build Tool**: Maven 3.8+
- **Migration**: Flyway

## Project Structure
```
src/main/java/com/onlineEducationPlatform/userModule/
├── config/                # Security configurations
│   ├── SecurityConfig.java
│   └── JwtAuthenticationFilter.java
├── controller/           # REST endpoints
│   └── UserController.java
├── service/             # Business logic
│   ├── UserService.java
│   ├── UserServiceImpl.java
│   └── JwtService.java
├── repository/          # Data access layer
│   └── UserRepository.java
├── entity/             # Database entities
│   ├── User.java
│   └── UserRole.java
├── dto/                # Data Transfer Objects
│   ├── UserDto.java
│   └── LoginDto.java
├── mapper/             # Object mappers
│   └── UserMapper.java
├── util/              # Utility classes
│   └── IdGenerator.java
└── UserModuleApplication.java
```

## Features

### 1. User Management
- User registration with email validation
- Role-based access control (Student, Instructor, Admin)
- Profile management and updates
- Secure password handling with BCrypt

### 2. Security Implementation
- JWT-based authentication
- HTTP-only cookies for token storage
- Role-based authorization
- Password encryption
- CORS configuration for frontend integration

### 3. Custom ID Generation
- 6-digit unique identifier
- Configurable length via properties
- Collision detection and handling

## API Documentation

### Authentication Endpoints

#### 1. Register User
```http
POST /api/auth/register
Content-Type: application/json

Request Body:
{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "securePassword123",
    "role": "STUDENT"
}

Response: (201 Created)
{
    "message": "User created successfully",
    "user": {
        "name": "John Doe",
        "email": "john@example.com",
        "role": "STUDENT"
    }
}
```

#### 2. Login
```http
POST /api/auth/login
Content-Type: application/json

Request Body:
{
    "email": "john@example.com",
    "password": "securePassword123"
}

Response: (200 OK)
{
    "message": "Login successful",
    "user": {
        "name": "John Doe",
        "email": "john@example.com",
        "role": "STUDENT"
    },
    "token": "jwt_token"
}
```

#### 3. Logout
```http
POST /api/auth/logout

Response: (200 OK)
{
    "message": "Logout successful"
}
```

### User Operations

#### 1. Get Profile
```http
GET /api/users/profile
Authorization: Bearer {token}

Response: (200 OK)
{
    "user": {
        "name": "John Doe",
        "email": "john@example.com",
        "role": "STUDENT"
    }
}
```

#### 2. Update Profile
```http
PUT /api/users/profile
Authorization: Bearer {token}
Content-Type: application/json

Request Body:
{
    "name": "John Doe Updated",
    "email": "john@example.com",
    "password": "newPassword123"
}

Response: (200 OK)
{
    "message": "Profile updated successfully",
    "user": {
        "name": "John Doe Updated",
        "email": "john@example.com",
        "role": "STUDENT"
    }
}
```

## Configuration

### Database Configuration
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/online_education_platform
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
```

### JWT Configuration
```properties
jwt.secret=your_jwt_secret_key
jwt.expiration=86400000
```

### Cookie Configuration
```properties
cookie.jwt.name=jwt-token
cookie.jwt.max-age=86400
```

## Database Schema
```sql
CREATE TABLE users (
    id VARCHAR(6) NOT NULL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    version BIGINT DEFAULT 0,
    CONSTRAINT email_unique UNIQUE (email)
);
```

## Setup and Installation

### Prerequisites
- JDK 17
- Maven 3.8+
- MySQL 8.0+

### Build and Run
```bash
# Clone repository
git clone [repository-url]

# Navigate to project
cd userModule

# Build project
mvn clean install

# Run application
mvn spring-boot:run
```

## Data and Security Flow

### Registration Flow
1. Validate UserDto
2. Check email uniqueness
3. Encode password
4. Generate unique ID
5. Save user
6. Return success response

### Authentication Flow
1. Validate credentials
2. Generate JWT token
3. Set HTTP-only cookie
4. Return user details

### Protected Operations Flow
1. Validate JWT token
2. Check user permissions
3. Process request
4. Return response

## Error Handling
- Global exception handling
- Validation error responses
- Custom error messages
- HTTP status mapping

## Future Enhancements
1. Email verification
2. Password reset functionality
3. OAuth2 integration
4. Rate limiting
5. Enhanced audit logging

## Contributors
[Your Name] - Initial work

## License
This project is licensed under the MIT License
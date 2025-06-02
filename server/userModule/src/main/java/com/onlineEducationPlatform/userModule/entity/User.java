package com.onlineEducationPlatform.userModule.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
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
@Table(name = "users", indexes = {
    @Index(name = "email_idx", columnList = "email", unique = true)
})
public class User {
    
    @Id
    @GeneratedValue(generator = "custom-id")
    @GenericGenerator(
        name = "custom-id",
        type = com.onlineEducationPlatform.userModule.util.IdGenerator.class
    )
    @Column(name = "id", nullable = false)
    private String id;

    @Column(nullable = false, length = 50)
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @Column(unique = true, nullable = false, length = 100)
    @Email(message = "Invalid email format")
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Version
    private Long version;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
package com.onlineEducationPlatform.userModule.repository;

import com.onlineEducationPlatform.userModule.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByEmailIgnoreCase(String email);
    
    Optional<User> findByEmailIgnoreCase(String email);
    
    Optional<User> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    Optional<User> findByEmailAndRole(String email, String role);
}
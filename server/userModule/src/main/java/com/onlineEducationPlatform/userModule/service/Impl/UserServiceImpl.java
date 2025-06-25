package com.onlineEducationPlatform.userModule.service.Impl;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import java.util.EnumSet;
import java.util.regex.Pattern;

import com.onlineEducationPlatform.userModule.service.Impl.JwtService;
import com.onlineEducationPlatform.userModule.dto.LoginDto;
import com.onlineEducationPlatform.userModule.dto.UserDto;
import com.onlineEducationPlatform.userModule.entity.User;
import com.onlineEducationPlatform.userModule.entity.UserRole;
import com.onlineEducationPlatform.userModule.mapper.UserMapper;
import com.onlineEducationPlatform.userModule.repository.UserRepository;
import com.onlineEducationPlatform.userModule.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Value("${cookie.jwt.name}")
    private String jwtCookieName;

    @Value("${cookie.jwt.max-age}")
    private int cookieMaxAge;

    @Override
    public UserDto registerUser(UserDto userDto) {

        userDto.setEmail(userDto.getEmail().toLowerCase());

        validatePassword(userDto.getPassword());

        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        if (!EnumSet.allOf(UserRole.class).contains(userDto.getRole())) {

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid role");
    }

        User user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        try {
            User savedUser = userRepository.save(user);
            return userMapper.toDto(savedUser);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating user");
        }
    }

    @Override
    public UserDto login(LoginDto loginDto, HttpServletResponse response) {
        logger.info("Attempting to login user with email: {}", loginDto.getEmail());
        User user = userRepository.findByEmail(loginDto.getEmail())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
    
        logger.info("User with email {} found", loginDto.getEmail());
    

        logger.info("Raw password from request: {}", loginDto.getPassword());
        logger.info("Stored encoded password: {}", user.getPassword());
    

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        
        logger.info("User with email {} authenticated successfully", loginDto.getEmail());
    
        if (!user.getRole().name().equals(loginDto.getRole())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid role");
        }
    
        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());
        setJwtCookie(response, token);
    
        UserDto userDto = userMapper.toDto(user);
        userDto.setToken(token);
        return userDto;
    }
    
    @Override
    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie(jwtCookieName, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);  // Add this
        cookie.setSecure(true);  
        response.addCookie(cookie);
    }

    @Override
    public UserDto getUserDetails(String token) {
        String email = jwtService.extractUsername(token);
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return userMapper.toDto(user);
    }

    @Override
    public UserDto updateUserDetails(String token, UserDto userDto) {
        String email = jwtService.extractUsername(token);
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        user.setName(userDto.getName());
        if (userDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }

    private void setJwtCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(jwtCookieName, token);
        cookie.setMaxAge(cookieMaxAge);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    private void validatePassword(String password) {
        if (password == null || password.length() < 6) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Password must be at least 6 characters long");
        }
        
        // Check for at least one uppercase letter
        if (!Pattern.compile("[A-Z]").matcher(password).find()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Password must contain at least one uppercase letter");
        }
        
        // Check for at least one number
        if (!Pattern.compile("[0-9]").matcher(password).find()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Password must contain at least one number");
        }
    }
}
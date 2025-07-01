package com.onlineEducationPlatform.userModule.controller;

import com.onlineEducationPlatform.userModule.dto.LoginDto;
import com.onlineEducationPlatform.userModule.dto.UserDto;
import com.onlineEducationPlatform.userModule.entity.User;
import com.onlineEducationPlatform.userModule.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class UserController {



    private final UserService userService;

    @PostMapping("/auth/register")
    public ResponseEntity<Map<String, Object>> registerUser(@Valid @RequestBody UserDto userDto) {
        UserDto registeredUser = userService.registerUser(userDto);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "User created successfully");
        response.put("user", registeredUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<Map<String, Object>> login(
            @Valid @RequestBody LoginDto loginDto,
            HttpServletResponse response) {
        UserDto loggedInUser = userService.login(loginDto, response);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "User logged in successfully");
        responseBody.put("user", loggedInUser);
        responseBody.put("token", loggedInUser.getToken());

        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletResponse response) {
        userService.logout(response);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "User logged out successfully");

        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/users/profile")
    public ResponseEntity<Map<String, Object>> getUserDetails(
            @RequestHeader("Authorization") String token) {
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        UserDto userDetails = userService.getUserDetails(jwtToken);

        Map<String, Object> response = new HashMap<>();
        response.put("user", userDetails);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/users/profile")
    public ResponseEntity<Map<String, Object>> updateUserDetails(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody UserDto userDto) {
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        UserDto updatedUser = userService.updateUserDetails(jwtToken, userDto);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "User updated successfully");
        response.put("user", updatedUser);

        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception e) {
        Map<String, String> response = new HashMap<>();
        response.put("message", e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    // Fetch all users
    @GetMapping("/all/users")
    public ResponseEntity<Map<String, Object>> getAllUsers(
            @RequestHeader("Authorization") String token) {
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        List<User> users = userService.getAllUsers();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Users fetched successfully");
        response.put("users", users);

        return ResponseEntity.ok(response);
    }

    // Delete user by ID
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Map<String, Object>> deleteUserById(
            @RequestHeader("Authorization") String token,
            @PathVariable String userId) {
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;

        try {
            userService.deleteUserById(userId);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "User with ID " + userId + " has been deleted successfully");

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", e.getMessage());

            return ResponseEntity.status(404).body(response);
        }
    }
}

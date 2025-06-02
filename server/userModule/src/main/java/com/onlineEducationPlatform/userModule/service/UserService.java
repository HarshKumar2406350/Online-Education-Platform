package com.onlineEducationPlatform.userModule.service;

import com.onlineEducationPlatform.userModule.dto.LoginDto;
import com.onlineEducationPlatform.userModule.dto.UserDto;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {
    UserDto registerUser(UserDto userDto);
    UserDto login(LoginDto loginDto, HttpServletResponse response);
    void logout(HttpServletResponse response);
    UserDto getUserDetails(String token);
    UserDto updateUserDetails(String token, UserDto userDto);
}
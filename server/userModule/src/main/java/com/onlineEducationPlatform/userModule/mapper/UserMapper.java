package com.onlineEducationPlatform.userModule.mapper;

import com.onlineEducationPlatform.userModule.dto.UserDto;
import com.onlineEducationPlatform.userModule.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public static UserDto toDto(User user) {
        return UserDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    public static User toEntity(UserDto userDto) {
        return User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .role(userDto.getRole())
                .build();
    }

    public static UserDto toLoginResponse(User user, String token) {

        return UserDto.builder()
        .id(user.getId())       // Add id
        .name(user.getName())
        .email(user.getEmail())
        .role(user.getRole())
        .token(token)           // Add token
        .build();
    }
}
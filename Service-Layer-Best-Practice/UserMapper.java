package com.example.global_exception_handler.mapper;

import com.example.global_exception_handler.dto.UserRequestDTO;
import com.example.global_exception_handler.dto.UserResponseDTO;
import com.example.global_exception_handler.entity.User;

public class UserMapper {

    public static User toEntity(UserRequestDTO dto) {
        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }

    public static UserResponseDTO toDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
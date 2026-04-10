package com.example.global_exception_handler.dtos;


import com.example.global_exception_handler.dtos.UserRequestDTO;
import com.example.global_exception_handler.dtos.UserResponseDTO;
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
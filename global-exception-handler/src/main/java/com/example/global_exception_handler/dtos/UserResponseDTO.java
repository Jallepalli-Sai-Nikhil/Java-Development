package com.example.global_exception_handler.dtos;

import com.example.global_exception_handler.entity.User;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {

    private Integer id;
    private String name;
    private String email;
}
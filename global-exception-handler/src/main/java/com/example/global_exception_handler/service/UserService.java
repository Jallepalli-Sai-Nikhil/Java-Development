package com.example.global_exception_handler.service;

import com.example.global_exception_handler.entity.User;

import java.util.List;

import com.example.global_exception_handler.dtos.UserRequestDTO;
import com.example.global_exception_handler.dtos.UserResponseDTO;

import java.util.List;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO requestDTO);

    UserResponseDTO getUserById(Integer id);

    List<UserResponseDTO> getAllUsers();

    void deleteUser(Integer id);
}

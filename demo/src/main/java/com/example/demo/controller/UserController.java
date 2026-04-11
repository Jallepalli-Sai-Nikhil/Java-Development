package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.UserRequestDTO;
import com.example.demo.dto.UserResponseDTO;
import com.example.demo.service.UserService;
import java.util.List;

import org.springframework.http.ResponseEntity;
import com.example.demo.dto.ApiResponse;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDTO>> createUser(@RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO createdUser = userService.save(userRequestDTO);
        ApiResponse<UserResponseDTO> response = new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "User created successfully",
                createdUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        ApiResponse<List<UserResponseDTO>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Users fetched successfully",
                users);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUserById(@PathVariable Long id) {
        UserResponseDTO userResponseDTO = userService.getUserById(id);
        ApiResponse<UserResponseDTO> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "User fetched successfully",
                userResponseDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateUser(
            @PathVariable Long id,
            @RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO updatedUser = userService.updateUser(id, userRequestDTO);
        ApiResponse<UserResponseDTO> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "User updated successfully",
                updatedUser);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> partialUpdateUser(
            @PathVariable Long id,
            @RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO updatedUser = userService.partialUpdateUser(id, userRequestDTO);
        ApiResponse<UserResponseDTO> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "User partially updated successfully",
                updatedUser);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        ApiResponse<Void> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "User deleted successfully",
                null);
        return ResponseEntity.ok(response);
    }

}

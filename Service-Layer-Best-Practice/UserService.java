package com.example.global_exception_handler.service.impl;

import com.example.global_exception_handler.dto.UserRequestDTO;
import com.example.global_exception_handler.dto.UserResponseDTO;
import com.example.global_exception_handler.entity.User;
import com.example.global_exception_handler.exception.UserNotFoundException;
import com.example.global_exception_handler.mapper.UserMapper;
import com.example.global_exception_handler.repository.UserRepository;
import com.example.global_exception_handler.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    // 🔹 CREATE
    @Override
    public UserResponseDTO createUser(UserRequestDTO requestDTO) {
        log.info("Creating user with email: {}", requestDTO.getEmail());

        validateUser(requestDTO);

        User user = UserMapper.toEntity(requestDTO);
        User savedUser = userRepository.save(user);

        log.info("User created with id: {}", savedUser.getId());

        return UserMapper.toDTO(savedUser);
    }

    // 🔹 GET BY ID
    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Integer id) {
        log.info("Fetching user with id: {}", id);

        User user = findUserOrThrow(id);

        return UserMapper.toDTO(user);
    }

    // 🔹 GET ALL
    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        log.info("Fetching all users");

        return userRepository.findAll()
                .stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    // 🔹 UPDATE
    @Override
    public UserResponseDTO updateUser(Integer id, UserRequestDTO requestDTO) {
        log.info("Updating user with id: {}", id);

        User existingUser = findUserOrThrow(id);

        existingUser.setName(requestDTO.getName());
        existingUser.setEmail(requestDTO.getEmail());

        User updatedUser = userRepository.save(existingUser);

        log.info("User updated with id: {}", updatedUser.getId());

        return UserMapper.toDTO(updatedUser);
    }

    // 🔹 DELETE
    @Override
    public void deleteUser(Integer id) {
        log.info("Deleting user with id: {}", id);

        User user = findUserOrThrow(id);

        userRepository.delete(user);

        log.info("User deleted with id: {}", id);
    }

    // 🔥 PRIVATE HELPER METHODS (Best Practice)

    private User findUserOrThrow(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found with id: " + id));
    }

    private void validateUser(UserRequestDTO requestDTO) {
        if (requestDTO.getEmail() == null || requestDTO.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
    }
}
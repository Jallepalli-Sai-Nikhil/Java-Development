package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.entity.User;
import com.example.demo.dto.UserRequestDTO;
import com.example.demo.dto.UserResponseDTO;
import com.example.demo.repository.UserRepository;
import com.example.demo.utils.UserMapper;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserResponseDTO save(UserRequestDTO userRequestDTO) {
        log.info("creating user with email" + userRequestDTO.getEmail());
        validateUser(userRequestDTO);

        User user = UserMapper.toEntity(userRequestDTO);
        User savedUser = userRepository.save(user);

        log.info("User created with id: {}", savedUser.getId());
        return UserMapper.toDTO(savedUser);

    }

    @Transactional
    public UserResponseDTO getUserById(Long id) {
        log.info("Fetching user with id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return UserMapper.toDTO(user);
    }

    public List<UserResponseDTO> getAllUsers() {
        log.info("Fetching all users");
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteUserById(Long id) {
        log.info("Deleting user with id: {}", id);
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
        log.info("User deleted with id: {}", id);
    }

    public UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO) {
        log.info("Updating user with id: {}", id);
        validateUser(userRequestDTO);

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        existingUser.setName(userRequestDTO.getName());
        existingUser.setEmail(userRequestDTO.getEmail());

        User updatedUser = userRepository.save(existingUser);
        log.info("User updated with id: {}", updatedUser.getId());
        return UserMapper.toDTO(updatedUser);
    }

    public UserResponseDTO partialUpdateUser(Long id, UserRequestDTO userRequestDTO) {
        log.info("Partially updating user with id: {}", id);
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        if (userRequestDTO.getName() != null && !userRequestDTO.getName().isBlank()) {
            existingUser.setName(userRequestDTO.getName());
        }
        if (userRequestDTO.getEmail() != null && !userRequestDTO.getEmail().isBlank()) {
            existingUser.setEmail(userRequestDTO.getEmail());
        }

        User updatedUser = userRepository.save(existingUser);
        log.info("User partially updated with id: {}", updatedUser.getId());
        return UserMapper.toDTO(updatedUser);
    }

    private void validateUser(UserRequestDTO requestDTO) {
        if (requestDTO.getEmail() == null || requestDTO.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
    }
}

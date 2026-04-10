package com.example.global_exception_handler.service;

import com.example.global_exception_handler.dtos.UserMapper;
import com.example.global_exception_handler.dtos.UserRequestDTO;
import com.example.global_exception_handler.dtos.UserResponseDTO;
import com.example.global_exception_handler.entity.User;
import com.example.global_exception_handler.exceptions.UserNotFoundException;
import com.example.global_exception_handler.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserResponseDTO createUser(UserRequestDTO requestDTO) {
        log.info("Creating user with name " + requestDTO.getName());

        User user = UserMapper.toEntity(requestDTO);
        User savedUser = userRepository.save(user);

        return  UserMapper.toDTO(savedUser);
    }

    @Override
    public UserResponseDTO getUserById(Integer id) {
        log.info("Fetching user with id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        return UserMapper.toDTO(user);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        log.info("Fetching all users");

        return userRepository.findAll()
                .stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    @Override
    public void deleteUser(Integer id) {
        log.info("Deleting user with id: {}", id);

        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found with id: " + id);
        }

        userRepository.deleteById(id);
    }

}

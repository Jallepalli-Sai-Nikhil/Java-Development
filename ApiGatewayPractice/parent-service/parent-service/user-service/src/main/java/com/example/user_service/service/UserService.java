package com.example.user_service.service;



import com.example.user_service.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAllUsers();

    Optional<User> getUserById(Long id);

    User createUser(User user);

    boolean existsByEmail(String email);
}
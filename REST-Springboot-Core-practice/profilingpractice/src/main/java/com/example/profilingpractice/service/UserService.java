package com.example.profilingpractice.service;

import com.example.profilingpractice.entity.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    User getUser(Long id);
    List<User> getUsers();
    User updateUser(Long id, User user);
    void deleteUser(Long id);
}

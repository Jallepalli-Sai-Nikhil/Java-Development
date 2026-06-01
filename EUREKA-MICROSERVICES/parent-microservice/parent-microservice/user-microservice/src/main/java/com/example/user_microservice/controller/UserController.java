package com.example.user_microservice.controller;

import com.example.user_microservice.entity.*;
import com.example.user_microservice.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    // Create user
    @PostMapping
    public User create(@RequestBody User user) {
        return service.save(user);
    }

    // Used by Feign (Order Service)
    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // Get all users
    @GetMapping
    public List<User> getAll() {
        return service.getAll();
    }
}

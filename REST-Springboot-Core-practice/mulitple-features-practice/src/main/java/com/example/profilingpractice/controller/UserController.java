package com.example.profilingpractice.controller;


import com.example.profilingpractice.entity.User;
import com.example.profilingpractice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public User create(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @PostMapping("/upsert")
    public User upsert(@RequestBody User user) {
        return userService.upsertUser(user);
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @GetMapping
    public List<User> getAll() {
        return userService.getUsers();
    }

    @PutMapping("/{id}")
    public User update(
            @PathVariable Long id,
            @RequestBody User user) {

        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {

        userService.deleteUser(id);
        return "Deleted Successfully";
    }

    @PostMapping("/{id}/restore")
    public User restore(@PathVariable Long id) {
        return userService.restoreUser(id);
    }
}
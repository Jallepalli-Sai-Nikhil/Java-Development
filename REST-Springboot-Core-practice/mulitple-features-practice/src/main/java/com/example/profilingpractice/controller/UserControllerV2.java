package com.example.profilingpractice.controller;

import com.example.profilingpractice.dto.UserDto;
import com.example.profilingpractice.entity.User;
import com.example.profilingpractice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v2/users")
@RequiredArgsConstructor
public class UserControllerV2 {

    private final UserService userService;

    @PostMapping
    public UserDto create(@RequestBody User user) {
        User saved = userService.saveUser(user);
        return toDto(saved);
    }

    @PostMapping("/upsert")
    public UserDto upsert(@RequestBody User user) {
        User saved = userService.upsertUser(user);
        return toDto(saved);
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable Long id) {
        return toDto(userService.getUser(id));
    }

    @GetMapping
    public List<UserDto> getAll() {
        return userService.getUsers().stream().map(this::toDto).collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public UserDto update(
            @PathVariable Long id,
            @RequestBody User user) {

        return toDto(userService.updateUser(id, user));
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {

        userService.deleteUser(id);
        return "Deleted Successfully";
    }

    @PostMapping("/{id}/restore")
    public UserDto restore(@PathVariable Long id) {
        return toDto(userService.restoreUser(id));
    }

    private UserDto toDto(User u) {
        return new UserDto(u.getId(), u.getName(), u.getEmail(), u.getCity());
    }
}


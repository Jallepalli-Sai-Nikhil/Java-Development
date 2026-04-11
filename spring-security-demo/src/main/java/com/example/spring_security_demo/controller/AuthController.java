package com.example.spring_security_demo.controller;

import com.example.spring_security_demo.dto.AuthRequest;
import com.example.spring_security_demo.dto.AuthResponse;
import com.example.spring_security_demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthController {

    @Autowired
    private AuthService service;

    @PostMapping("/register")
    public String register(@RequestBody AuthRequest req) {
        return service.register(req);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest req) {
        return service.login(req);
    }

    @PostMapping("/refresh")
    public Map<String, String> refresh(@RequestBody Map<String, String> req) {

        String newAccess = service.refresh(req.get("refreshToken"));

        return Map.of("accessToken", newAccess);
    }
}
package com.example.spring_security_demo.service;

import com.example.spring_security_demo.dto.AuthRequest;
import com.example.spring_security_demo.dto.AuthResponse;
import com.example.spring_security_demo.entity.AppUser;
import com.example.spring_security_demo.repository.UserRepository;
import com.example.spring_security_demo.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authManager;

    public String register(AuthRequest req) {

        AppUser user = new AppUser();
        user.setUsername(req.getUsername());
        user.setPassword(encoder.encode(req.getPassword()));
        user.setRole("USER");

        repo.save(user);

        return "User Registered";
    }

    public AuthResponse login(AuthRequest req) {

        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.getUsername(),
                        req.getPassword()
                )
        );

        if (auth.isAuthenticated()) {

            String access = jwtUtil.generateAccessToken(req.getUsername());
            String refresh = jwtUtil.generateRefreshToken(req.getUsername());

            return new AuthResponse(access, refresh);
        }

        throw new RuntimeException("Invalid credentials");
    }

    public String refresh(String refreshToken) {

        if (jwtUtil.isValid(refreshToken)) {
            String username = jwtUtil.extractUsername(refreshToken);
            return jwtUtil.generateAccessToken(username);
        }

        throw new RuntimeException("Invalid refresh token");
    }
}
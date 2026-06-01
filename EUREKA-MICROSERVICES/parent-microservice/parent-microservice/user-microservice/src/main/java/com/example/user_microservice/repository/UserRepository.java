package com.example.user_microservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.user_microservice.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}

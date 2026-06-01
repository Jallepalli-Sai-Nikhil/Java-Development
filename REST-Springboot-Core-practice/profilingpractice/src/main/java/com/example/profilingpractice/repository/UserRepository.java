package com.example.profilingpractice.repository;

import com.example.profilingpractice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

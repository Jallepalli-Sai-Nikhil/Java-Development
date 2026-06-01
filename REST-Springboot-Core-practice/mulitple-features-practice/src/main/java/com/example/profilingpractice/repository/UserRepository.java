package com.example.profilingpractice.repository;

import com.example.profilingpractice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
	List<User> findAllByDeletedFalse();
	Optional<User> findByIdAndDeletedFalse(Long id);
	Optional<User> findByEmail(String email);
}

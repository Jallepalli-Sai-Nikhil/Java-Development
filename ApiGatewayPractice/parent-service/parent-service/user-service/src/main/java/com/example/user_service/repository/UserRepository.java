package com.example.user_service.repository;

import com.example.user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JpaRepository<User, Long>
 * ─────────────────────────────────────────────────────────────────
 * - User → the entity type this repository manages
 * - Long → the type of the entity's primary key
 *
 * Spring Data JPA generates ALL the SQL at runtime — you write zero
 * boilerplate JDBC code. Out of the box you get:
 * save(), findById(), findAll(), deleteById(), existsById(), count() …
 *
 * Custom query by method name: Spring parses "findByEmail" and
 * generates SELECT * FROM users WHERE email = ? automatically.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
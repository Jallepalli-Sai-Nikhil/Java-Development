package com.example.order_service.client;

import com.example.order_service.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * UserClient — Feign declarative HTTP client
 * ─────────────────────────────────────────────────────────────────
 * This is just an INTERFACE. No implementation class needed.
 * Spring Cloud OpenFeign generates a proxy at runtime.
 *
 * @FeignClient(name = "user-service")
 *                   ─────────────────────────────────────────────────────────────────
 *                   name = "user-service"
 *                   This must EXACTLY match the spring.application.name of the
 *                   target service (user-service's application.yml).
 *
 *                   At runtime, Feign asks Eureka:
 *                   "Give me the host:port of the service registered as
 *                   'user-service'"
 *                   Eureka responds: "It's at 192.168.x.x:8081"
 *                   Feign then makes the HTTP call to that address.
 *
 *                   This means:
 *                   ✅ No hardcoded URLs or ports
 *                   ✅ Works even if user-service moves to a different port
 *                   ✅ Load balancing if multiple user-service instances are
 *                   running
 *
 *                   The method signatures mirror the endpoints in
 *                   UserController:
 *                   UserController: @GetMapping("/{id}")
 *                   UserClient: @GetMapping("/api/users/{id}") ← full path here
 */
@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/api/users/{id}")
    UserResponse getUserById(@PathVariable("id") Long id);
}
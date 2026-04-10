package com.example.order_service.controller;

import com.example.order_service.client.UserClient;
import com.example.order_service.dto.OrderRequest;
import com.example.order_service.dto.UserResponse;
import com.example.order_service.entity.Order;
import com.example.order_service.repository.OrderRepository;
import feign.FeignException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Slf4j (Lombok) — injects a `log` field backed by SLF4J.
 *        Use: log.info(), log.error(), log.debug() etc.
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderRepository orderRepository;

    /**
     * UserClient is a FEIGN PROXY, injected just like any Spring bean.
     * Calling userClient.getUserById(id) will:
     * 1. Look up "user-service" in Eureka → get host:port
     * 2. Make GET http://host:port/api/users/{id}
     * 3. Deserialise the JSON response into UserResponse
     * 4. Return it — or throw FeignException on error
     */
    private final UserClient userClient;

    /**
     * GET /api/orders
     * All orders in the system.
     */
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderRepository.findAll());
    }

    /**
     * GET /api/orders/{id}
     * Single order by its own ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return orderRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/orders/user/{userId}
     * All orders placed by a specific user.
     * Also calls user-service via Feign to validate user exists
     * and enrich the log message.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable Long userId) {
        try {
            // Validate: does this user actually exist in user-service?
            UserResponse user = userClient.getUserById(userId);
            log.info("Fetching orders for user: {} ({})", user.getName(), user.getEmail());
        } catch (FeignException.NotFound e) {
            // user-service returned 404 — user doesn't exist
            log.warn("User {} not found in user-service", userId);
            return ResponseEntity.notFound().build();
        } catch (FeignException e) {
            // Any other Feign error (500, timeout, service down, etc.)
            log.error("Error calling user-service: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }

        List<Order> orders = orderRepository.findByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    /**
     * POST /api/orders
     *
     * Flow:
     * 1. Validate request body (@Valid)
     * 2. Call user-service via Feign to verify user exists
     * 3. Build and save the Order entity
     * 4. Return 201 Created with the saved order
     *
     * If user-service is down or returns 404, we return an appropriate error.
     */
    @PostMapping
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderRequest request) {

        // Step 1: Verify user exists — Feign call to user-service
        UserResponse user;
        try {
            user = userClient.getUserById(request.getUserId());
            log.info("Creating order for user: {} <{}>", user.getName(), user.getEmail());
        } catch (FeignException.NotFound e) {
            // Return 404 with a meaningful message in the body
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("User with id " + request.getUserId() + " does not exist");
        } catch (FeignException e) {
            log.error("Failed to reach user-service: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Could not verify user — user-service unavailable");
        }

        // Step 2: Build and persist the Order
        Order order = Order.builder()
                .userId(request.getUserId())
                .productName(request.getProductName())
                .amount(BigDecimal.valueOf(request.getAmount()))
                .build();
        // createdAt is set automatically by @PrePersist on the entity

        Order saved = orderRepository.save(order);
        log.info("Order {} created successfully for user {}", saved.getId(), user.getName());

        return ResponseEntity.status(HttpStatus.CREATED).body(saved); // 201
    }
}
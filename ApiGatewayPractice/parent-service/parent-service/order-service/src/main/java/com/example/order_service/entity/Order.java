package com.example.order_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "userId is required")
    @Column(nullable = false)
    private Long userId;
    // We store only the userId, NOT a @ManyToOne relation to a User entity.
    // The User entity lives in a DIFFERENT service (different database).
    // Cross-service JPA joins are an anti-pattern in microservices.

    @NotBlank(message = "Product name is required")
    @Column(nullable = false)
    private String productName;

    @Positive(message = "Amount must be positive")
    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    // @PrePersist — JPA lifecycle hook: runs just BEFORE INSERT.
    // Automatically sets the timestamp so callers don't need to supply it.
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
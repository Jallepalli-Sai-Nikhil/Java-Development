package com.example.order_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

/**
 * OrderRequest — incoming JSON body for POST /api/orders
 *
 * WHY a separate DTO and not use the Order entity directly?
 * ─────────────────────────────────────────────────────────────────
 * 1. Security: Never expose @Id or auto-set fields (createdAt) to callers.
 * 2. Separation: Request shape vs. persistence shape can differ.
 * 3. Validation: Can have different @Valid rules for input vs. DB constraints.
 */
@Data
public class UserResponse {
    private Long id;
    private String name;
    private String email;
}
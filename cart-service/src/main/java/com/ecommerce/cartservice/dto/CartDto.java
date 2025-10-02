package com.ecommerce.cartservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for Cart information.
 * This DTO handles cart data transfer between layers.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    
    private Long id;
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    private String sessionId;
    
    private BigDecimal totalAmount;
    
    private Integer totalItems;
    
    private Boolean isActive;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private List<CartItemDto> cartItems;
}

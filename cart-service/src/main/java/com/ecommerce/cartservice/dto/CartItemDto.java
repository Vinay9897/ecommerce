package com.ecommerce.cartservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for CartItem information.
 * This DTO handles cart item data transfer between layers.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {
    
    private Long id;
    
    @NotNull(message = "Product ID is required")
    private Long productId;
    
    @NotNull(message = "Product name is required")
    private String productName;
    
    private String productDescription;
    
    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be non-negative")
    private BigDecimal price;
    
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
    
    private String imageUrl;
    
    private LocalDateTime addedAt;
    
    private LocalDateTime updatedAt;
    
    /**
     * Calculate total price for this cart item
     */
    public BigDecimal getTotalPrice() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}

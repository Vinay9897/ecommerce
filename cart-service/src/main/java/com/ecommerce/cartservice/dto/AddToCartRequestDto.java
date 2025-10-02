package com.ecommerce.cartservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Data Transfer Object for adding items to cart.
 * This DTO handles add to cart request data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartRequestDto {
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
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
}

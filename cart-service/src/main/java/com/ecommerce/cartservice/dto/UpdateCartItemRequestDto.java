package com.ecommerce.cartservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object for updating cart item quantity.
 * This DTO handles cart item update requests.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCartItemRequestDto {
    
    @NotNull(message = "Cart item ID is required")
    private Long cartItemId;
    
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
}

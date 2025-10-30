package com.ecommerce.inventoryservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDto {

    private Long id;

    @NotNull(message = "productId is required")
    private Long productId;

    @NotNull(message = "availableQuantity is required")
    @PositiveOrZero(message = "availableQuantity must be zero or positive")
    private Integer availableQuantity;

    @NotNull(message = "reservedQuantity is required")
    @PositiveOrZero(message = "reservedQuantity must be zero or positive")
    private Integer reservedQuantity = 0;

    @NotNull(message = "minimumStockLevel is required")
    @PositiveOrZero(message = "minimumStockLevel must be zero or positive")
    private Integer minimumStockLevel = 0;

    @NotNull(message = "maximumStockLevel is required")
    @PositiveOrZero(message = "maximumStockLevel must be zero or positive")
    private Integer maximumStockLevel = 1000;

    private LocalDateTime lastRestockedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Helper methods
    public Integer getTotalQuantity() {
        return availableQuantity + reservedQuantity;
    }

    public boolean isLowStock() {
        return availableQuantity <= minimumStockLevel;
    }

    public boolean isStockAvailable(Integer requestedQuantity) {
        return availableQuantity >= requestedQuantity;
    }
}

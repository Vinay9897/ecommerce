package com.ecommerce.inventoryservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "productId is required")
    @Column(nullable = false, unique = true)
    private Long productId;

    @NotNull(message = "available Quantity is required")
    @PositiveOrZero(message = "available Quantity must be zero or positive")
    @Column(nullable = false)
    private Integer availableQuantity;

    @NotNull(message = "reservedQuantity is required")
    @PositiveOrZero(message = "reservedQuantity must be zero or positive")
    @Column(nullable = false)
    private Integer reservedQuantity = 0;

    @NotNull(message = "minimumStockLevel is required")
    @PositiveOrZero(message = "minimumStockLevel must be zero or positive")
    @Column(nullable = false)
    private Integer minimumStockLevel = 0;

    @NotNull(message = "maximumStockLevel is required")
    @PositiveOrZero(message = "maximumStockLevel must be zero or positive")
    @Column(nullable = false)
    private Integer maximumStockLevel = 1000;

    @Column(name = "last_restocked_at")
    private LocalDateTime lastRestockedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();  
        updatedAt = LocalDateTime.now();
    }      

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Helper method to get total quantity
    public Integer getTotalQuantity() {
        return availableQuantity + reservedQuantity;
    }

    // Helper method to check if stock is low
    public boolean isLowStock() {
        return availableQuantity <= minimumStockLevel;
    }


    

    // Helper method to check if stock is available
    public boolean isStockAvailable(Integer requestedQuantity) {
        return availableQuantity >= requestedQuantity;
    }
} 
   

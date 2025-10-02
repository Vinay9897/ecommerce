package com.ecommerce.cartservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * CartItem entity representing individual items in a shopping cart.
 * This entity manages item details within a cart.
 */
@Entity
@Table(name = "cart_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    @JsonBackReference
    private Cart cart;
    
    @Column(name = "product_id", nullable = false)
    @NotNull(message = "Product ID is required")
    private Long productId;
    
    @Column(name = "product_name", nullable = false)
    @NotNull(message = "Product name is required")
    private String productName;
    
    @Column(name = "product_description")
    private String productDescription;
    
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be non-negative")
    private BigDecimal price;
    
    @Column(name = "quantity", nullable = false)
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
    
    @Column(name = "image_url")
    private String imageUrl;
    
    @Column(name = "added_at")
    private LocalDateTime addedAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        addedAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    /**
     * Calculate total price for this cart item
     */
    public BigDecimal getTotalPrice() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}

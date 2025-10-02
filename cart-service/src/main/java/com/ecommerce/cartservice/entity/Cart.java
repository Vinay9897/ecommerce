package com.ecommerce.cartservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Cart entity representing a shopping cart.
 * This entity manages cart information and cart items.
 */
@Entity
@Table(name = "carts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @Column(name = "session_id")
    private String sessionId;
    
    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;
    
    @Column(name = "total_items")
    private Integer totalItems = 0;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference
    private List<CartItem> cartItems = new ArrayList<>();
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    /**
     * Calculate and update total amount and item count
     */
    public void calculateTotals() {
        this.totalAmount = cartItems.stream()
            .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
            
        this.totalItems = cartItems.stream()
            .mapToInt(CartItem::getQuantity)
            .sum();
    }
    
    /**
     * Add item to cart
     */
    public void addItem(CartItem item) {
        item.setCart(this);
        this.cartItems.add(item);
        calculateTotals();
    }
    
    /**
     * Remove item from cart
     */
    public void removeItem(CartItem item) {
        this.cartItems.remove(item);
        calculateTotals();
    }
}

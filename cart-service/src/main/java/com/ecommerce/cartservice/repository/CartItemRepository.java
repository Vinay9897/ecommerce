package com.ecommerce.cartservice.repository;

import com.ecommerce.cartservice.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for CartItem entity operations.
 * This interface handles database operations for cart item management.
 */
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    
    /**
     * Find cart item by cart ID and product ID
     * @param cartId Cart ID to search in
     * @param productId Product ID to search for
     * @return Optional containing CartItem if found
     */
    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);
    
    /**
     * Find all cart items for a specific cart
     * @param cartId Cart ID to get items for
     * @return List of cart items
     */
    List<CartItem> findByCartId(Long cartId);
    
    /**
     * Find cart item by ID and user ID (for security validation)
     * @param cartItemId Cart item ID
     * @param userId User ID for validation
     * @return Optional containing CartItem if found and belongs to user
     */
    @Query("SELECT ci FROM CartItem ci JOIN ci.cart c WHERE ci.id = :cartItemId AND c.userId = :userId")
    Optional<CartItem> findByIdAndUserId(@Param("cartItemId") Long cartItemId, @Param("userId") Long userId);
    
    /**
     * Delete all cart items for a specific cart
     * @param cartId Cart ID to clear items for
     */
    void deleteByCartId(Long cartId);
    
    /**
     * Count items in a specific cart
     * @param cartId Cart ID to count items for
     * @return Number of items in cart
     */
    Integer countByCartId(Long cartId);
}

package com.ecommerce.cartservice.repository;

import com.ecommerce.cartservice.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Cart entity operations.
 * This interface handles database operations for cart management.
 */
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    
    /**
     * Find active cart by user ID
     * @param userId User ID to search for
     * @return Optional containing Cart if found
     */
    Optional<Cart> findByUserIdAndIsActiveTrue(Long userId);
    
    /**
     * Find cart by user ID regardless of active status
     * @param userId User ID to search for
     * @return Optional containing Cart if found
     */
    Optional<Cart> findByUserId(Long userId);
    
    /**
     * Find cart by session ID
     * @param sessionId Session ID to search for
     * @return Optional containing Cart if found
     */
    Optional<Cart> findBySessionId(String sessionId);
    
    /**
     * Check if user has an active cart
     * @param userId User ID to check
     * @return true if user has active cart, false otherwise
     */
    boolean existsByUserIdAndIsActiveTrue(Long userId);
    
    /**
     * Get total items count for user's active cart
     * @param userId User ID to get count for
     * @return Total items count
     */
    @Query("SELECT COALESCE(SUM(ci.quantity), 0) FROM Cart c JOIN c.cartItems ci WHERE c.userId = :userId AND c.isActive = true")
    Integer getTotalItemsCountByUserId(@Param("userId") Long userId);
    
    /**
     * Deactivate cart (soft delete)
     * @param userId User ID whose cart to deactivate
     */
    @Query("UPDATE Cart c SET c.isActive = false WHERE c.userId = :userId")
    void deactivateCartByUserId(@Param("userId") Long userId);
}

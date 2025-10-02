package com.ecommerce.cartservice.interfaces;

import com.ecommerce.cartservice.dto.AddToCartRequestDto;
import com.ecommerce.cartservice.dto.CartDto;
import com.ecommerce.cartservice.dto.UpdateCartItemRequestDto;
import com.ecommerce.cartservice.entity.Cart;

/**
 * Service interface for cart operations.
 * This interface defines the contract for cart business logic.
 */
public interface CartService {
    
    /**
     * Get cart by user ID
     * @param userId User ID to get cart for
     * @return Cart DTO if found, null otherwise
     */
    CartDto getCartByUserId(Long userId);
    
    /**
     * Add item to cart
     * @param addToCartRequest Request containing item details
     * @return Updated cart DTO
     */
    CartDto addToCart(AddToCartRequestDto addToCartRequest);
    
    /**
     * Update cart item quantity
     * @param updateRequest Request containing item ID and new quantity
     * @return Updated cart DTO
     */
    CartDto updateCartItem(UpdateCartItemRequestDto updateRequest);
    
    /**
     * Remove item from cart
     * @param cartItemId Cart item ID to remove
     * @param userId User ID for security validation
     * @return Updated cart DTO
     */
    CartDto removeFromCart(Long cartItemId, Long userId);
    
    /**
     * Clear entire cart for user
     * @param userId User ID whose cart to clear
     * @return Empty cart DTO
     */
    CartDto clearCart(Long userId);
    
    /**
     * Get cart item count for user
     * @param userId User ID to get count for
     * @return Total number of items in cart
     */
    Integer getCartItemCount(Long userId);
    
    /**
     * Create new cart for user
     * @param userId User ID to create cart for
     * @return New cart entity
     */
    Cart createCartForUser(Long userId);
}

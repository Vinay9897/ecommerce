package com.ecommerce.cartservice.exception;

/**
 * Custom exception for cart item not found errors.
 * This exception is thrown when a requested cart item doesn't exist.
 */
public class CartItemNotFoundException extends RuntimeException {
    
    public CartItemNotFoundException(String message) {
        super(message);
    }
    
    public CartItemNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

package com.ecommerce.cartservice.exception;

/**
 * Custom exception for cart-related errors.
 * This exception handles cart operation failures and related errors.
 */
public class CartException extends RuntimeException {
    
    public CartException(String message) {
        super(message);
    }
    
    public CartException(String message, Throwable cause) {
        super(message, cause);
    }
}

package com.ecommerce.authservice.exception;

/**
 * Custom exception for authentication-related errors.
 * This exception will handle authentication failures and related errors.
 */
public class AuthenticationException extends RuntimeException {
    
    public AuthenticationException(String message) {
        super(message);
    }
    
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}

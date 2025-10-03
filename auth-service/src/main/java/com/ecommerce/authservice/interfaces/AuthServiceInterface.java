package com.ecommerce.authservice.interfaces;

import com.ecommerce.authservice.dto.AuthRequestDto;
import com.ecommerce.authservice.entity.User;

/**
 * Service interface for authentication operations.
 * This interface defines the contract for authentication business logic.
 */
public interface AuthServiceInterface {
    
    /**
     * Authenticate user with username and password
     * @param authRequest Authentication request containing credentials
     * @return JWT token if authentication successful
     */
    String authenticate(AuthRequestDto authRequest);
    
    /**
     * Register a new user
     * @param user User entity to register
     * @return Registered user entity
     */
    User registerUser(User user);
    
    /**
     * Validate JWT token
     * @param token JWT token to validate
     * @return true if token is valid, false otherwise
     */
    boolean validateToken(String token);
    
    /**
     * Extract username from JWT token
     * @param token JWT token
     * @return username extracted from token
     */
    String extractUsername(String token);
    
    /**
     * Find user by username
     * @param username Username to search for
     * @return User entity if found, null otherwise
     */
    User findUserByUsername(String username);
}

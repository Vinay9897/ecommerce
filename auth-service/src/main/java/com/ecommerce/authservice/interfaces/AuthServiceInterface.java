package com.ecommerce.authservice.interfaces;

import org.springframework.stereotype.Service;

import com.ecommerce.authservice.dto.AuthRequestDto;
import com.ecommerce.authservice.dto.RegisterRequest;
import com.ecommerce.authservice.dto.LoginRequest;
import com.ecommerce.authservice.dto.AuthResponse;
import com.ecommerce.authservice.entity.User;

/**
 * Service interface for authentication operations.
 * This interface defines the contract for authentication business logic.
 */
@Service    
public interface AuthServiceInterface {
    
    /**
     * Authenticate user with username and password
     * @param authRequest Authentication request containing credentials
     * @return JWT token if authentication successful
     */
    String authenticate(AuthRequestDto authRequest);

    /**
     * Authenticate user using login request (username or email)
     * @param loginRequest login credentials
     * @return AuthResponse with tokens
     */
    AuthResponse authenticate(LoginRequest loginRequest);
    
    /**
     * Register a new user
     * @param user User entity to register
     * @return Registered user entity
     */
    User registerUser(User user);

    /**
     * Register a new user from request, ensuring ROLE_USER is assigned
     * @param request RegisterRequest containing username, email, password
     * @return AuthResponse or user details
     */
    AuthResponse register(RegisterRequest request);
    
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

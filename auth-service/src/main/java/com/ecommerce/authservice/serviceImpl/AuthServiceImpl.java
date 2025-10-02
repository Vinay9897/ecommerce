package com.ecommerce.authservice.serviceImpl;

import com.ecommerce.authservice.dto.AuthRequestDto;
import com.ecommerce.authservice.entity.User;
import com.ecommerce.authservice.interfaces.AuthService;
import com.ecommerce.authservice.repository.UserRepository;
import com.ecommerce.authservice.exception.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implementation of AuthService interface.
 * This class provides the actual implementation of authentication business logic.
 */
@Service
public class AuthServiceImpl implements AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public String authenticate(AuthRequestDto authRequest) {
        // TODO: Implement JWT token generation after successful authentication
        User user = findUserByUsername(authRequest.getUsername());
        if (user == null) {
            throw new AuthenticationException("User not found");
        }
        
        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new AuthenticationException("Invalid credentials");
        }
        
        // Generate and return JWT token
        return generateJwtToken(user);
    }
    
    @Override
    public User registerUser(User user) {
        // Check if user already exists
        if (findUserByUsername(user.getUsername()) != null) {
            throw new AuthenticationException("User already exists");
        }
        
        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        return userRepository.save(user);
    }
    
    @Override
    public boolean validateToken(String token) {
        // TODO: Implement JWT token validation logic
        try {
            // JWT validation logic will be implemented here
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public String extractUsername(String token) {
        // TODO: Implement username extraction from JWT token
        // JWT username extraction logic will be implemented here
        return null;
    }
    
    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
    
    private String generateJwtToken(User user) {
        // TODO: Implement JWT token generation
        // JWT token generation logic will be implemented here
        return "temporary-jwt-token";
    }
}

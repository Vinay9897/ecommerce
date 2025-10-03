package com.ecommerce.authservice.service;

import com.ecommerce.authservice.dto.AuthRequestDto;
import com.ecommerce.authservice.dto.RegisterRequest;
import com.ecommerce.authservice.dto.LoginRequest;
import com.ecommerce.authservice.dto.AuthResponse;
import com.ecommerce.authservice.entity.User;
import com.ecommerce.authservice.entity.Role;
import com.ecommerce.authservice.interfaces.AuthServiceInterface;
import com.ecommerce.authservice.repository.UserRepository;
import com.ecommerce.authservice.repository.RoleRepository;
import com.ecommerce.authservice.exception.AuthenticationException;
import com.ecommerce.authservice.security.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Set;

/**
 * Implementation of AuthService interface.
 * This class provides the actual implementation of authentication business logic.
 */
@Service
public class AuthService implements AuthServiceInterface {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    
    public AuthService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtils jwtUtils,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }
    
    @Override
    public String authenticate(AuthRequestDto authRequest) {
        User user = findUserByUsername(authRequest.getUsername());
        if (user == null) {
            throw new AuthenticationException("User not found");
        }
        
        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new AuthenticationException("Invalid credentials");
        }
        
        // Generate and return JWT token
        UserDetails principal = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Set.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        return jwtUtils.generateAccessToken(principal);
    }

    @Override
    public AuthResponse authenticate(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword())
        );
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String accessToken = jwtUtils.generateAccessToken(principal);
        return AuthResponse.builder()
                .accessToken(accessToken)
                .tokenType("Bearer")
                .refreshToken(null)
                .build();
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
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AuthenticationException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AuthenticationException("Email already exists");
        }

        Role userRole = roleRepository.findByName("ROLE_USER").orElseGet(() -> {
            Role r = new Role();
            r.setName("ROLE_USER");
            return roleRepository.save(r);
        });

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(java.util.Set.of(userRole));
        user = userRepository.save(user);

        UserDetails principal = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Set.of(new SimpleGrantedAuthority(userRole.getName()))
        );

        String accessToken = jwtUtils.generateAccessToken(principal);
        return AuthResponse.builder()
                .accessToken(accessToken)
                .tokenType("Bearer")
                .refreshToken(null)
                .build();
    }
    
    @Override
    public boolean validateToken(String token) {
        return jwtUtils.validateToken(token);
    }
    
    @Override
    public String extractUsername(String token) {
        return jwtUtils.getUsernameFromToken(token);
    }
    
    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
    
    // token generation is delegated to JwtUtils
}

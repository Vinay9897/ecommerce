package com.ecommerce.authservice.repository;

import com.ecommerce.authservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for User entity operations.
 * This interface handles database operations for user management.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Find user by username
     * @param username Username to search for
     * @return Optional containing User if found
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Find user by email
     * @param email Email to search for
     * @return Optional containing User if found
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Check if user exists by username
     * @param username Username to check
     * @return true if user exists, false otherwise
     */
    boolean existsByUsername(String username);
    
    /**
     * Check if user exists by email
     * @param email Email to check
     * @return true if user exists, false otherwise
     */
    boolean existsByEmail(String email);
}

package com.ecommerce.cartservice.controller;

import com.ecommerce.cartservice.entity.Cart;
import com.ecommerce.cartservice.repository.CartRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CartController {

    private final CartRepository cartRepository;

    
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/addToCart")
    public ResponseEntity<Cart> addToCart(@RequestParam Long userId,
                                          @RequestParam Long productId,
                                          @RequestParam(defaultValue = "1") Integer quantity) {
        if (quantity == null || quantity <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Cart existing = cartRepository.findByUserIdAndProductId(userId, productId);
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity);
            Cart updated = cartRepository.save(existing);
            return ResponseEntity.ok(updated);
        }

        Cart newCart = new Cart();
        newCart.setUserId(userId);
        newCart.setProductId(productId);
        newCart.setQuantity(quantity);
        Cart created = cartRepository.save(newCart);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cart> findById(@PathVariable Long id) {
        return cartRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Cart>> findByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(cartRepository.findByUserId(userId));
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}")
    public ResponseEntity<Cart> update(@PathVariable Long id, @Valid @RequestBody Cart cart) {
        return cartRepository.findById(id)
                .map(existingCart -> {
                    existingCart.setUserId(cart.getUserId());
                    existingCart.setProductId(cart.getProductId());
                    existingCart.setQuantity(cart.getQuantity());
                    Cart updated = cartRepository.save(existingCart);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (cartRepository.existsById(id)) {
            cartRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

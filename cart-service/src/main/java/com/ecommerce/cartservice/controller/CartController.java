package com.ecommerce.cartservice.controller;

import com.ecommerce.cartservice.entity.Cart;
import com.ecommerce.cartservice.repository.CartRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CartController {

    private final CartRepository cartRepository;

    @PostMapping("createCart")
    public ResponseEntity<Cart> create (@RequestBody Cart cart) {
    // public ResponseEntity<Cart> create (@PathVariable Long userId, @PathVariable Long productId) {

       Cart saved = cartRepository.save(cart);
    //  Cart saved = cartRepository.findByProductId(productId).get();

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<Cart>> findAll() {
        return ResponseEntity.ok(cartRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cart> findById(@PathVariable Long id) {
        return cartRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Cart>> findByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(cartRepository.findByUserId(userId));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Cart>> findByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(cartRepository.findByProductId(productId));
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (cartRepository.existsById(id)) {
            cartRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

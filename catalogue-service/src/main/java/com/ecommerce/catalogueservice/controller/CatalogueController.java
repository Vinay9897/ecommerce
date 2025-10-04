package com.ecommerce.catalogueservice.controller;

import com.ecommerce.catalogueservice.entity.Catalogue;
import com.ecommerce.catalogueservice.repository.CatalogueRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalogue")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CatalogueController {

    private final CatalogueRepository catalogueRepository;

    @PostMapping
    public ResponseEntity<Catalogue> create(@Valid @RequestBody Catalogue catalogue) {
        Catalogue saved = catalogueRepository.save(catalogue);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<Catalogue>> findAll() {
        return ResponseEntity.ok(catalogueRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Catalogue> findById(@PathVariable Long id) {
        return catalogueRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}



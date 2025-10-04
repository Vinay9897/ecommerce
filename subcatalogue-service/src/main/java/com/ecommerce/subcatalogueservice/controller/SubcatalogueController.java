package com.ecommerce.subcatalogueservice.controller;

import com.ecommerce.subcatalogueservice.entity.Subcatalogue;
import com.ecommerce.subcatalogueservice.repository.SubcatalogueRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subcatalogues")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SubcatalogueController {

    private final SubcatalogueRepository subcatalogueRepository;

    @PostMapping("/addSubcatalogue")
    public ResponseEntity<Subcatalogue> create(@Valid @RequestBody Subcatalogue subcatalogue) {
        Subcatalogue saved = subcatalogueRepository.save(subcatalogue);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/getAllSubcatalogues")
    public ResponseEntity<List<Subcatalogue>> findAll() {
        return ResponseEntity.ok(subcatalogueRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subcatalogue> findById(@PathVariable Long id) {
        return subcatalogueRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}



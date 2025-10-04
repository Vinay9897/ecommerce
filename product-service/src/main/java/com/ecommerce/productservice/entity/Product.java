package com.ecommerce.productservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "catalogueId is required")
    @Column(nullable = false)
    private Long catalogueId;

    @NotBlank(message = "catalogueName is required")
    @Column(nullable = false)
    private String catalogueName;

    @NotNull(message = "subCatalogueId is required")
    @Column(nullable = false)
    private Long subCatalogueId;

    @NotBlank(message = "subCatalogueName is required")
    @Column(nullable = false)
    private String subCatalogueName;

    @NotBlank(message = "productName is required")
    @Column(nullable = false)
    private String productName;

    private String shortDescription;

    private String brand;

    private String material;

    @NotNull(message = "basePrice is required")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal basePrice;

    @Column(nullable = false)
    private Boolean isReturnable = Boolean.TRUE;

    private String returnPolicy;

    @Column(length = 4000)
    private String longDescription;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

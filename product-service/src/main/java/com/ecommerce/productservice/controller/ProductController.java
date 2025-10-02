package com.ecommerce.productservice.controller;

import com.ecommerce.productservice.dto.ProductDto;
import com.ecommerce.productservice.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "Product Management", description = "APIs for managing products in the e-commerce system")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Get all products", description = "Retrieve a list of all products in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all products",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDto.class)))
    })
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        log.info("GET request to fetch all products");
        List<ProductDto> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Retrieve a specific product using its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDto.class))),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<ProductDto> getProductById(@Parameter(description = "Product ID", required = true) @PathVariable Long id) {
        log.info("GET request to fetch product with id: {}", id);
        ProductDto product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get products by category", description = "Retrieve all products that belong to a specific category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDto.class)))
    })
    public ResponseEntity<List<ProductDto>> getProductsByCategory(@Parameter(description = "Product category name", required = true) @PathVariable String category) {
        log.info("GET request to fetch products by category: {}", category);
        List<ProductDto> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search")
    @Operation(summary = "Search products by name", description = "Search for products using case-insensitive partial name matching")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products found successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDto.class)))
    })
    public ResponseEntity<List<ProductDto>> searchProductsByName(@Parameter(description = "Product name or partial name to search for", required = true) @RequestParam String name) {
        log.info("GET request to search products by name: {}", name);
        List<ProductDto> products = productService.searchProductsByName(name);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/price-range")
    @Operation(summary = "Filter products by price range", description = "Retrieve products within a specified price range (inclusive)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products filtered successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDto.class)))
    })
    public ResponseEntity<List<ProductDto>> getProductsByPriceRange(
            @Parameter(description = "Minimum price (inclusive)", required = true) @RequestParam BigDecimal minPrice,
            @Parameter(description = "Maximum price (inclusive)", required = true) @RequestParam BigDecimal maxPrice) {
        log.info("GET request to fetch products with price range: {} - {}", minPrice, maxPrice);
        List<ProductDto> products = productService.getProductsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }

    @PostMapping
    @Operation(summary = "Create a new product", description = "Add a new product to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Product already exists")
    })
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto) {
        log.info("POST request to create new product: {}", productDto.getName());
        ProductDto createdProduct = productService.createProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing product", description = "Update product information by providing the product ID and new data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "409", description = "Product name already exists")
    })
    public ResponseEntity<ProductDto> updateProduct(
            @Parameter(description = "Product ID to update", required = true) @PathVariable Long id,
            @Valid @RequestBody ProductDto productDto) {
        log.info("PUT request to update product with id: {}", id);
        ProductDto updatedProduct = productService.updateProduct(id, productDto);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product", description = "Remove a product from the system using its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<Void> deleteProduct(@Parameter(description = "Product ID to delete", required = true) @PathVariable Long id) {
        log.info("DELETE request to delete product with id: {}", id);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}

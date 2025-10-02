package com.ecommerce.productservice.service;

import com.ecommerce.productservice.dto.ProductDto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service interface for Product operations.
 * This interface defines the contract for product-related business operations.
 */
public interface ProductService {

    /**
     * Retrieves all products from the system.
     *
     * @return List of all products as ProductDto
     */
    List<ProductDto> getAllProducts();

    /**
     * Retrieves a product by its unique identifier.
     *
     * @param id the unique identifier of the product
     * @return ProductDto representing the found product
     * @throws com.ecommerce.productservice.exception.ProductNotFoundException if product not found
     */
    ProductDto getProductById(Long id);

    /**
     * Retrieves products by category.
     *
     * @param category the category to filter products by
     * @return List of products in the specified category
     */
    List<ProductDto> getProductsByCategory(String category);

    /**
     * Searches for products by name (case-insensitive partial match).
     *
     * @param name the name or partial name to search for
     * @return List of products matching the search criteria
     */
    List<ProductDto> searchProductsByName(String name);

    /**
     * Retrieves products within a specified price range.
     *
     * @param minPrice the minimum price (inclusive)
     * @param maxPrice the maximum price (inclusive)
     * @return List of products within the price range
     */
    List<ProductDto> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

    /**
     * Creates a new product in the system.
     *
     * @param productDto the product data to create
     * @return ProductDto representing the created product
     * @throws com.ecommerce.productservice.exception.ProductAlreadyExistsException if product with same name exists
     */
    ProductDto createProduct(ProductDto productDto);

    /**
     * Updates an existing product.
     *
     * @param id the unique identifier of the product to update
     * @param productDto the updated product data
     * @return ProductDto representing the updated product
     * @throws com.ecommerce.productservice.exception.ProductNotFoundException if product not found
     * @throws com.ecommerce.productservice.exception.ProductAlreadyExistsException if updated name conflicts with existing product
     */
    ProductDto updateProduct(Long id, ProductDto productDto);

    /**
     * Deletes a product from the system.
     *
     * @param id the unique identifier of the product to delete
     * @throws com.ecommerce.productservice.exception.ProductNotFoundException if product not found
     */
    void deleteProduct(Long id);
}

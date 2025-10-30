package com.ecommerce.inventoryservice.controller;

import com.ecommerce.inventoryservice.dto.InventoryDto;
import com.ecommerce.inventoryservice.service.InventoryService;
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

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "Inventory Management", description = "APIs for managing inventory in the e-commerce system")
public class InventoryController {

    private final InventoryService inventoryService; 

    @PostMapping("/create")
    @Operation(summary = "Create inventory for a product", description = "Create inventory record for a new product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Inventory created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = InventoryDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Inventory already exists for this product")
    })
    public ResponseEntity<InventoryDto> createInventory(@Valid @RequestBody InventoryDto inventoryDto) {
        log.info("POST request to create inventory for product: {}", inventoryDto.getProductId());
        InventoryDto createdInventory = inventoryService.createInventory(inventoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInventory);
    }

    @GetMapping("/getInventoryById/{id}")
    @Operation(summary = "Get inventory by ID", description = "Retrieve inventory information using its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory found successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = InventoryDto.class))),
            @ApiResponse(responseCode = "404", description = "Inventory not found")
    })
    public ResponseEntity<InventoryDto> getInventoryById(@Parameter(description = "Inventory ID", required = true) @PathVariable Long id) {
        log.info("GET request to fetch inventory with id: {}", id);
        InventoryDto inventory = inventoryService.getInventoryById(id);
        return ResponseEntity.ok(inventory);
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Get inventory by product ID", description = "Retrieve inventory information for a specific product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory found successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = InventoryDto.class))),
            @ApiResponse(responseCode = "404", description = "Inventory not found")
    })
    public ResponseEntity<InventoryDto> getInventoryByProductId(@Parameter(description = "Product ID", required = true) @PathVariable Long productId) {
        log.info("GET request to fetch inventory for product: {}", productId);
        InventoryDto inventory = inventoryService.getInventoryByProductId(productId);
        return ResponseEntity.ok(inventory);
    }

    @GetMapping("/getAllInventory")
    @Operation(summary = "Get all inventory", description = "Retrieve a list of all inventory records")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all inventory",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = InventoryDto.class)))
    })
    public ResponseEntity<List<InventoryDto>> getAllInventory() {
        log.info("GET request to fetch all inventory");
        List<InventoryDto> inventory = inventoryService.getAllInventory();
        return ResponseEntity.ok(inventory);
    }

    @GetMapping("/low-stock")
    @Operation(summary = "Get low stock items", description = "Retrieve all products with stock below minimum threshold")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Low stock items retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = InventoryDto.class)))
    })
    public ResponseEntity<List<InventoryDto>> getLowStockItems() {
        log.info("GET request to fetch low stock items");
        List<InventoryDto> lowStockItems = inventoryService.getLowStockItems();
        return ResponseEntity.ok(lowStockItems);
    }

    @GetMapping("/out-of-stock")
    @Operation(summary = "Get out of stock items", description = "Retrieve all products with zero available stock")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Out of stock items retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = InventoryDto.class)))
    })
    public ResponseEntity<List<InventoryDto>> getOutOfStockItems() {
        log.info("GET request to fetch out of stock items");
        List<InventoryDto> outOfStockItems = inventoryService.getOutOfStockItems();
        return ResponseEntity.ok(outOfStockItems);
    }

    @GetMapping("/products")
    @Operation(summary = "Get inventory by product IDs", description = "Retrieve inventory information for multiple products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = InventoryDto.class)))
    })
    public ResponseEntity<List<InventoryDto>> getInventoryByProductIds(@Parameter(description = "List of product IDs", required = true) @RequestParam List<Long> productIds) {
        log.info("GET request to fetch inventory for products: {}", productIds);
        List<InventoryDto> inventory = inventoryService.getInventoryByProductIds(productIds);
        return ResponseEntity.ok(inventory);
    }

    @PutMapping("updateInventory/{id}")
    @Operation(summary = "Update inventory", description = "Update inventory information by providing the inventory ID and new data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = InventoryDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Inventory not found")
    })
    public ResponseEntity<InventoryDto> updateInventory(
            @Parameter(description = "Inventory ID to update", required = true) @PathVariable Long id,
            @Valid @RequestBody InventoryDto inventoryDto) {
        log.info("PUT request to update inventory with id: {}", id);
        InventoryDto updatedInventory = inventoryService.updateInventory(id, inventoryDto);
        return ResponseEntity.ok(updatedInventory);
    }
 
    @PutMapping("/product/{productId}/stock")
    @Operation(summary = "Update stock quantity", description = "Update the available stock quantity for a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = InventoryDto.class))),
            @ApiResponse(responseCode = "404", description = "Inventory not found")
    })
    public ResponseEntity<InventoryDto> updateStock(
            @Parameter(description = "Product ID", required = true) @PathVariable Long productId,
            @Parameter(description = "New stock quantity", required = true) @RequestParam Integer quantity) {
        log.info("PUT request to update stock for product: {} to quantity: {}", productId, quantity);
        InventoryDto updatedInventory = inventoryService.updateStock(productId, quantity);
        return ResponseEntity.ok(updatedInventory);
    }

    @PutMapping("/product/{productId}/reserve")
    @Operation(summary = "Reserve stock", description = "Reserve a specific quantity of stock for a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock reserved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = InventoryDto.class))),
            @ApiResponse(responseCode = "400", description = "Insufficient stock"),
            @ApiResponse(responseCode = "404", description = "Inventory not found")
    })
    public ResponseEntity<InventoryDto> reserveStock(
            @Parameter(description = "Product ID", required = true) @PathVariable Long productId,
            @Parameter(description = "Quantity to reserve", required = true) @RequestParam Integer quantity) {
        log.info("PUT request to reserve stock for product: {} quantity: {}", productId, quantity);
        InventoryDto updatedInventory = inventoryService.reserveStock(productId, quantity);
        return ResponseEntity.ok(updatedInventory);
    }

    @PutMapping("/product/{productId}/release")
    @Operation(summary = "Release reserved stock", description = "Release previously reserved stock for a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserved stock released successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = InventoryDto.class))),
            @ApiResponse(responseCode = "400", description = "Insufficient reserved stock"),
            @ApiResponse(responseCode = "404", description = "Inventory not found")
    })
    public ResponseEntity<InventoryDto> releaseReservedStock(
            @Parameter(description = "Product ID", required = true) @PathVariable Long productId,
            @Parameter(description = "Quantity to release", required = true) @RequestParam Integer quantity) {
        log.info("PUT request to release reserved stock for product: {} quantity: {}", productId, quantity);
        InventoryDto updatedInventory = inventoryService.releaseReservedStock(productId, quantity);
        return ResponseEntity.ok(updatedInventory);
    }

    @PutMapping("/product/{productId}/restock")
    @Operation(summary = "Restock product", description = "Add more stock to a product's inventory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product restocked successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = InventoryDto.class))),
            @ApiResponse(responseCode = "404", description = "Inventory not found")
    })
    public ResponseEntity<InventoryDto> restock(
            @Parameter(description = "Product ID", required = true) @PathVariable Long productId,
            @Parameter(description = "Quantity to add", required = true) @RequestParam Integer quantity) {
        log.info("PUT request to restock product: {} with quantity: {}", productId, quantity);
        InventoryDto updatedInventory = inventoryService.restock(productId, quantity);
        return ResponseEntity.ok(updatedInventory);
    }

    @PutMapping("/product/{productId}/adjust")
    @Operation(summary = "Adjust stock", description = "Adjust stock quantity (positive or negative adjustment)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock adjusted successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = InventoryDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid adjustment"),
            @ApiResponse(responseCode = "404", description = "Inventory not found")
    })
    public ResponseEntity<InventoryDto> adjustStock(
            @Parameter(description = "Product ID", required = true) @PathVariable Long productId,
            @Parameter(description = "Adjustment quantity (positive or negative)", required = true) @RequestParam Integer adjustmentQuantity) {
        log.info("PUT request to adjust stock for product: {} by quantity: {}", productId, adjustmentQuantity);
        InventoryDto updatedInventory = inventoryService.adjustStock(productId, adjustmentQuantity);
        return ResponseEntity.ok(updatedInventory);
    }

    @GetMapping("/product/{productId}/check-availability")
    @Operation(summary = "Check stock availability", description = "Check if sufficient stock is available for a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock availability checked successfully")
    })
    public ResponseEntity<Boolean> checkStockAvailability(
            @Parameter(description = "Product ID", required = true) @PathVariable Long productId,
            @Parameter(description = "Required quantity", required = true) @RequestParam Integer quantity) {
        log.info("GET request to check stock availability for product: {} quantity: {}", productId, quantity);
        boolean isAvailable = inventoryService.isStockAvailable(productId, quantity);
        return ResponseEntity.ok(isAvailable);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete inventory", description = "Remove inventory record from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Inventory deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Inventory not found")
    })
    public ResponseEntity<Void> deleteInventory(@Parameter(description = "Inventory ID to delete", required = true) @PathVariable Long id) {
        log.info("DELETE request to delete inventory with id: {}", id);
        inventoryService.deleteInventory(id);
        return ResponseEntity.noContent().build();
    }
}

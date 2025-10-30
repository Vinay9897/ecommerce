package com.ecommerce.inventoryservice.serviceImpl;

import com.ecommerce.inventoryservice.dto.InventoryDto;
import com.ecommerce.inventoryservice.entity.Inventory;
import com.ecommerce.inventoryservice.exception.InsufficientStockException;
import com.ecommerce.inventoryservice.exception.InventoryNotFoundException;
import com.ecommerce.inventoryservice.repository.InventoryRepository;
import com.ecommerce.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Override
    public InventoryDto createInventory(InventoryDto inventoryDto) {
        log.info("Creating inventory for product: {}", inventoryDto.getProductId());
        
        if (inventoryRepository.existsByProductId(inventoryDto.getProductId())) {
            throw new RuntimeException("Inventory already exists for product: " + inventoryDto.getProductId());
        }
        
        Inventory inventory = new Inventory();
        BeanUtils.copyProperties(inventoryDto, inventory);
        
        Inventory savedInventory = inventoryRepository.save(inventory);
        return convertToDto(savedInventory);
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryDto getInventoryById(Long id) {
        log.info("Fetching inventory by id: {}", id);
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new InventoryNotFoundException("Inventory not found with id: " + id));
        return convertToDto(inventory);
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryDto getInventoryByProductId(Long productId) {
        log.info("Fetching inventory for product: {}", productId);
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new InventoryNotFoundException("Inventory not found for product: " + productId));
        return convertToDto(inventory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryDto> getAllInventory() {
        log.info("Fetching all inventory");
        return inventoryRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryDto> getLowStockItems() {
        log.info("Fetching low stock items");
        return inventoryRepository.findLowStockItems().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryDto> getOutOfStockItems() {
        log.info("Fetching out of stock items");
        return inventoryRepository.findOutOfStockItems().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryDto> getInventoryByProductIds(List<Long> productIds) {
        log.info("Fetching inventory for products: {}", productIds);
        return inventoryRepository.findByProductIdIn(productIds).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public InventoryDto updateInventory(Long id, InventoryDto inventoryDto) {
        log.info("Updating inventory with id: {}", id);
        Inventory existingInventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new InventoryNotFoundException("Inventory not found with id: " + id));
        
        BeanUtils.copyProperties(inventoryDto, existingInventory, "id", "productId", "createdAt");
        existingInventory.setUpdatedAt(LocalDateTime.now());
        
        Inventory updatedInventory = inventoryRepository.save(existingInventory);
        return convertToDto(updatedInventory);
    }

    @Override
    public InventoryDto updateStock(Long productId, Integer quantity) {
        log.info("Updating stock for product: {} to quantity: {}", productId, quantity);
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new InventoryNotFoundException("Inventory not found for product: " + productId));
        
        inventory.setAvailableQuantity(quantity);
        inventory.setUpdatedAt(LocalDateTime.now());
        
        Inventory updatedInventory = inventoryRepository.save(inventory);
        return convertToDto(updatedInventory);
    }

    @Override
    public InventoryDto reserveStock(Long productId, Integer quantity) {
        log.info("Reserving stock for product: {} quantity: {}", productId, quantity);
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new InventoryNotFoundException("Inventory not found for product: " + productId));
        
        if (inventory.getAvailableQuantity() < quantity) {
            throw new InsufficientStockException("Insufficient stock for product: " + productId + 
                    ". Available: " + inventory.getAvailableQuantity() + ", Requested: " + quantity);
        }
        
        inventory.setAvailableQuantity(inventory.getAvailableQuantity() - quantity);
        inventory.setReservedQuantity(inventory.getReservedQuantity() + quantity);
        inventory.setUpdatedAt(LocalDateTime.now());
        
        Inventory updatedInventory = inventoryRepository.save(inventory);
        return convertToDto(updatedInventory);
    }

    @Override
    public InventoryDto releaseReservedStock(Long productId, Integer quantity) {
        log.info("Releasing reserved stock for product: {} quantity: {}", productId, quantity);
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new InventoryNotFoundException("Inventory not found for product: " + productId));
        
        if (inventory.getReservedQuantity() < quantity) {
            throw new InsufficientStockException("Insufficient reserved stock for product: " + productId + 
                    ". Reserved: " + inventory.getReservedQuantity() + ", Requested: " + quantity);
        }
        
        inventory.setReservedQuantity(inventory.getReservedQuantity() - quantity);
        inventory.setAvailableQuantity(inventory.getAvailableQuantity() + quantity);
        inventory.setUpdatedAt(LocalDateTime.now());
        
        Inventory updatedInventory = inventoryRepository.save(inventory);
        return convertToDto(updatedInventory);
    }

    @Override
    public InventoryDto restock(Long productId, Integer quantity) {
        log.info("Restocking product: {} with quantity: {}", productId, quantity);
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new InventoryNotFoundException("Inventory not found for product: " + productId));
        
        inventory.setAvailableQuantity(inventory.getAvailableQuantity() + quantity);
        inventory.setLastRestockedAt(LocalDateTime.now());
        inventory.setUpdatedAt(LocalDateTime.now());
        
        Inventory updatedInventory = inventoryRepository.save(inventory);
        return convertToDto(updatedInventory);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isStockAvailable(Long productId, Integer quantity) {
        log.info("Checking stock availability for product: {} quantity: {}", productId, quantity);
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new InventoryNotFoundException("Inventory not found for product: " + productId));
        
        return inventory.isStockAvailable(quantity);
    }

    @Override
    public void deleteInventory(Long id) {
        log.info("Deleting inventory with id: {}", id);
        if (!inventoryRepository.existsById(id)) {
            throw new InventoryNotFoundException("Inventory not found with id: " + id);
        }
        inventoryRepository.deleteById(id);
    }

    @Override
    public InventoryDto adjustStock(Long productId, Integer adjustmentQuantity) {
        log.info("Adjusting stock for product: {} by quantity: {}", productId, adjustmentQuantity);
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new InventoryNotFoundException("Inventory not found for product: " + productId));
        
        int newQuantity = inventory.getAvailableQuantity() + adjustmentQuantity;
        if (newQuantity < 0) {
            throw new InsufficientStockException("Cannot adjust stock below zero for product: " + productId);
        }
        
        inventory.setAvailableQuantity(newQuantity);
        inventory.setUpdatedAt(LocalDateTime.now());
        
        Inventory updatedInventory = inventoryRepository.save(inventory);
        return convertToDto(updatedInventory);
    }

    private InventoryDto convertToDto(Inventory inventory) {
        InventoryDto inventoryDto = new InventoryDto();
        BeanUtils.copyProperties(inventory, inventoryDto);
        return inventoryDto;
    }
}

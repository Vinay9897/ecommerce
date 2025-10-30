package com.ecommerce.inventoryservice.service;

import com.ecommerce.inventoryservice.dto.InventoryDto;

import java.util.List;

public interface InventoryService {

    InventoryDto createInventory(InventoryDto inventoryDto);

    InventoryDto getInventoryById(Long id);

    InventoryDto getInventoryByProductId(Long productId);

    List<InventoryDto> getAllInventory();

    List<InventoryDto> getLowStockItems();

    List<InventoryDto> getOutOfStockItems();

    List<InventoryDto> getInventoryByProductIds(List<Long> productIds);

    InventoryDto updateInventory(Long id, InventoryDto inventoryDto);

    InventoryDto updateStock(Long productId, Integer quantity);

    InventoryDto reserveStock(Long productId, Integer quantity);

    InventoryDto releaseReservedStock(Long productId, Integer quantity);

    InventoryDto restock(Long productId, Integer quantity);

    boolean isStockAvailable(Long productId, Integer quantity);

    void deleteInventory(Long id);

    InventoryDto adjustStock(Long productId, Integer adjustmentQuantity);
}

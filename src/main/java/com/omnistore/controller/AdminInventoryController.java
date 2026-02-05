package com.omnistore.controller;

import com.omnistore.dto.UpdateStockRequestDto;
import com.omnistore.entity.Product;
import com.omnistore.services.InventoryService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/inventory")
public class AdminInventoryController {

    private final InventoryService inventoryService;

    public AdminInventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PutMapping("/product/{productId}")
    public Product updateProductStock(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateStockRequestDto dto
    ) {
        return inventoryService.updateStock(productId, dto.getStock());
    }
}

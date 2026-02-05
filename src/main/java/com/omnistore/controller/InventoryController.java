package com.omnistore.controller;

import com.omnistore.entity.Product;
import com.omnistore.services.InventoryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    // GET PRODUCT INVENTORY
    @GetMapping("/product/{productId}")
    public Product getProductById(@PathVariable Long productId) {
        return inventoryService.getProductById(productId);
    }
}

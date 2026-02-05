package com.omnistore.controller;

import com.omnistore.dto.ProductRequestDto;
import com.omnistore.dto.ProductResponseDto;
import com.omnistore.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ProductResponseDto createProduct(@Valid  @RequestBody ProductRequestDto dto) {
        return productService.createProduct(dto);
    }

    @GetMapping
    public List<ProductResponseDto> getProducts() {
        return productService.getAllProducts();
    }
}

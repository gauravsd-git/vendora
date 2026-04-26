package com.gaurav.vendora.controller;

import com.gaurav.vendora.payload.dto.ProductDto;
import com.gaurav.vendora.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(
            @RequestBody ProductDto dto,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {

        return ResponseEntity.ok(productService.createProduct(dto, jwt));
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {

        return ResponseEntity.ok(productService.getAllProducts(jwt));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductDto dto,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {

        return ResponseEntity.ok(productService.updateProduct(id, dto, jwt));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(
            @PathVariable Long id
    ) throws Exception {

        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }
}
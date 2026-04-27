package com.gaurav.vendora.controller;

import com.gaurav.vendora.payload.dto.ProductDto;
import com.gaurav.vendora.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','STORE_ADMIN','CASHIER')")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasRole('STORE_ADMIN')")
    public ResponseEntity<ProductDto> createProduct(
            @RequestBody ProductDto dto,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        return ResponseEntity.ok(productService.createProduct(dto, jwt));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('STORE_ADMIN','CASHIER')")
    public ResponseEntity<List<ProductDto>> getAllProducts(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        return ResponseEntity.ok(productService.getAllProducts(jwt));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('STORE_ADMIN')")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductDto dto,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        return ResponseEntity.ok(productService.updateProduct(id, dto, jwt));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('STORE_ADMIN')")
    public ResponseEntity<String> deleteProduct(
            @PathVariable Long id
    ) throws Exception {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }
}
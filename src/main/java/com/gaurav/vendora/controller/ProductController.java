package com.gaurav.vendora.controller;

import com.gaurav.vendora.exceptions.UserException;
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

    // Create
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto dto) throws UserException {
        return ResponseEntity.ok(productService.createProduct(dto));
    }

    // Get all
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAll() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // Get by store
    @GetMapping("/store")
    public ResponseEntity<List<ProductDto>> getByStore() throws UserException {
        return ResponseEntity.ok(productService.getProductsByStore());
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(
            @PathVariable Long id,
            @RequestBody ProductDto dto
    ) {
        return ResponseEntity.ok(productService.updateProduct(id, dto));
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted");
    }
}
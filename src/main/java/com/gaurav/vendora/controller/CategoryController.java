package com.gaurav.vendora.controller;

import com.gaurav.vendora.payload.dto.CategoryDto;
import com.gaurav.vendora.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','STORE_ADMIN','CASHIER')")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','STORE_ADMIN')")
    public ResponseEntity<CategoryDto> createCategory(
            @RequestBody CategoryDto dto,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        return ResponseEntity.ok(categoryService.createCategory(dto, jwt));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','STORE_ADMIN','CASHIER')")
    public ResponseEntity<List<CategoryDto>> getAllCategories(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        return ResponseEntity.ok(categoryService.getAllCategories(jwt));
    }
}
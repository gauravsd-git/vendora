package com.gaurav.vendora.controller;

import com.gaurav.vendora.payload.dto.CategoryDto;
import com.gaurav.vendora.service.CategoryService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(
            @RequestBody CategoryDto dto,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {

        return ResponseEntity.ok(categoryService.createCategory(dto, jwt));
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {

        return ResponseEntity.ok(categoryService.getAllCategories(jwt));
    }
}
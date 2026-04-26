package com.gaurav.vendora.service;

import com.gaurav.vendora.payload.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(CategoryDto dto, String jwt) throws Exception;

    List<CategoryDto> getAllCategories(String jwt) throws Exception;
}
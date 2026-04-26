package com.gaurav.vendora.service;

import com.gaurav.vendora.payload.dto.ProductDto;

import java.util.List;

public interface ProductService {

    ProductDto createProduct(ProductDto dto, String jwt) throws Exception;

    List<ProductDto> getAllProducts(String jwt) throws Exception;

    ProductDto updateProduct(Long id, ProductDto dto, String jwt) throws Exception;

    void deleteProduct(Long id) throws Exception;
}
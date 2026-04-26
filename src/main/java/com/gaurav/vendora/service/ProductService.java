package com.gaurav.vendora.service;

import com.gaurav.vendora.exceptions.UserException;
import com.gaurav.vendora.payload.dto.ProductDto;

import java.util.List;

public interface ProductService {

    ProductDto createProduct(ProductDto dto) throws UserException;

    List<ProductDto> getAllProducts();

    List<ProductDto> getProductsByStore() throws UserException;

    ProductDto updateProduct(Long id, ProductDto dto);

    void deleteProduct(Long id);
}
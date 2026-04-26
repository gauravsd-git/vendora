package com.gaurav.vendora.mapper;

import com.gaurav.vendora.model.Product;
import com.gaurav.vendora.payload.dto.ProductDto;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    // DTO → Entity
    public Product toEntity(ProductDto dto) {
        return Product.builder()
                .id(dto.getId())
                .name(dto.getName())
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .build();
    }

    // Entity → DTO
    public ProductDto toDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .storeId(product.getStore() != null ? product.getStore().getId() : null)
                .build();
    }
}
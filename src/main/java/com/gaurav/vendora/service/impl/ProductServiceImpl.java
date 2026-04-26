package com.gaurav.vendora.service.impl;

import com.gaurav.vendora.exceptions.UserException;
import com.gaurav.vendora.mapper.ProductMapper;
import com.gaurav.vendora.model.Product;
import com.gaurav.vendora.model.Store;
import com.gaurav.vendora.model.User;
import com.gaurav.vendora.payload.dto.ProductDto;
import com.gaurav.vendora.repository.ProductRepository;
import com.gaurav.vendora.service.ProductService;
import com.gaurav.vendora.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserService userService;

    @Override
    public ProductDto createProduct(ProductDto dto) throws UserException {

        User user = userService.getCurrentUser();

        Store store = user.getStore();
        if (store == null) {
            throw new RuntimeException("User is not assigned to any store");
        }

        Product product = ProductMapper.toEntity(dto);

        product.setStore(store);

        Product saved = productRepository.save(product);

        return ProductMapper.toDto(saved);
    }

    @Override
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(ProductMapper::toDto)
                .toList();
    }

    @Override
    public List<ProductDto> getProductsByStore() throws UserException {

        User user = userService.getCurrentUser();
        Store store = user.getStore();

        return productRepository.findByStoreId(store.getId())
                .stream()
                .map(ProductMapper::toDto)
                .toList();
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto dto) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());

        return ProductMapper.toDto(productRepository.save(product));
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
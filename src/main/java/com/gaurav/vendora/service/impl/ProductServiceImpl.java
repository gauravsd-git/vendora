package com.gaurav.vendora.service.impl;

import com.gaurav.vendora.configurtion.JwtProvider;
import com.gaurav.vendora.mapper.ProductMapper;
import com.gaurav.vendora.model.Product;
import com.gaurav.vendora.model.Store;
import com.gaurav.vendora.model.User;
import com.gaurav.vendora.payload.dto.ProductDto;
import com.gaurav.vendora.repository.ProductRepository;
import com.gaurav.vendora.repository.UserRepository;
import com.gaurav.vendora.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final ProductMapper productMapper;

    @Override
    public ProductDto createProduct(ProductDto dto, String jwt) throws Exception {

        String email = jwtProvider.getEmailFromToken(jwt);
        User user = userRepository.findByEmail(email);

        Store store = user.getStore();

        if (store == null) {
            throw new RuntimeException("User is not assigned to any store");
        }

        Product product = productMapper.toEntity(dto);

        // link store
        product.setStore(store);

        Product saved = productRepository.save(product);

        return productMapper.toDto(saved);
    }

    @Override
    public List<ProductDto> getAllProducts(String jwt) throws Exception {

        String email = jwtProvider.getEmailFromToken(jwt);
        User user = userRepository.findByEmail(email);
        Store store = user.getStore();

        if (store == null) {
            throw new RuntimeException("User is not assigned to any store");
        }

        List<Product> products = productRepository.findByStore(store);

        return products.stream()
                .map(productMapper::toDto)
                .toList();
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto dto, String jwt) throws Exception {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());

        Product updated = productRepository.save(product);

        return productMapper.toDto(updated);
    }

    @Override
    public void deleteProduct(Long id) throws Exception {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        productRepository.delete(product);
    }
}
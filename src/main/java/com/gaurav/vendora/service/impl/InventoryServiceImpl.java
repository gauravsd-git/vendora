package com.gaurav.vendora.service.impl;

import com.gaurav.vendora.exceptions.UserException;
import com.gaurav.vendora.model.Product;
import com.gaurav.vendora.model.User;
import com.gaurav.vendora.repository.ProductRepository;
import com.gaurav.vendora.service.InventoryService;
import com.gaurav.vendora.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final ProductRepository productRepository;
    private final UserService userService;

    @Override
    public void increaseStock(Long productId, int quantity) throws UserException {

        Product product = getAuthorizedProduct(productId);

        product.setQuantity(product.getQuantity() + quantity);

        productRepository.save(product);
    }

    @Override
    public void decreaseStock(Long productId, int quantity) throws UserException {

        Product product = getAuthorizedProduct(productId);

        if (product.getQuantity() < quantity) {
            throw new RuntimeException("Not enough stock");
        }

        product.setQuantity(product.getQuantity() - quantity);

        productRepository.save(product);
    }

    @Override
    public boolean isLowStock(Long productId) throws UserException {

        Product product = getAuthorizedProduct(productId);

        return product.getQuantity() < 5;
    }

    // 🔐 MULTI-TENANT SECURITY
    private Product getAuthorizedProduct(Long productId) throws UserException {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        User user = userService.getCurrentUser();

        if (!product.getStore().getId().equals(user.getStore().getId())) {
            throw new RuntimeException("Unauthorized access");
        }

        return product;
    }
}
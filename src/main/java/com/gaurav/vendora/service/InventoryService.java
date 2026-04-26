package com.gaurav.vendora.service;

import com.gaurav.vendora.exceptions.UserException;

public interface InventoryService {

    void increaseStock(Long productId, int quantity) throws UserException;

    void decreaseStock(Long productId, int quantity) throws UserException;

    boolean isLowStock(Long productId) throws UserException;
}
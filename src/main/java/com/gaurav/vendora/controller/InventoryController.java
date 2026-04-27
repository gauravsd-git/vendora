package com.gaurav.vendora.controller;

import com.gaurav.vendora.exceptions.UserException;
import com.gaurav.vendora.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','STORE_ADMIN','CASHIER')")
public class InventoryController {

    private final InventoryService inventoryService;

    @PutMapping("/increase/{productId}")
    public String increaseStock(
            @PathVariable Long productId,
            @RequestParam int qty
    ) throws UserException {
        inventoryService.increaseStock(productId, qty);
        return "Stock increased successfully";
    }

    @PutMapping("/decrease/{productId}")
    public String decreaseStock(
            @PathVariable Long productId,
            @RequestParam int qty
    ) throws UserException {
        inventoryService.decreaseStock(productId, qty);
        return "Stock decreased successfully";
    }

    @GetMapping("/low/{productId}")
    public boolean isLowStock(@PathVariable Long productId) throws UserException {
        return inventoryService.isLowStock(productId);
    }
}
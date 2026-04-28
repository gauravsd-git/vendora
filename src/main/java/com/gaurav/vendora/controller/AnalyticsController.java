package com.gaurav.vendora.controller;

import com.gaurav.vendora.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','STORE_ADMIN')")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/total-sales")
    public Map<String, Object> getTotalSales() {
        Map<String, Object> response = new HashMap<>();
        response.put("totalRevenue", analyticsService.getTotalSales());
        response.put("totalOrders", analyticsService.getTotalOrders());
        return response;
    }

    @GetMapping("/top-products")
    public List<Object[]> getTopProducts() {
        return analyticsService.getTopProducts();
    }
}
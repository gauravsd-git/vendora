package com.gaurav.vendora.repository;

import com.gaurav.vendora.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("""
        SELECT p.name, SUM(oi.quantity)
        FROM OrderItem oi
        JOIN oi.product p
        GROUP BY p.name
        ORDER BY SUM(oi.quantity) DESC
    """)
    List<Object[]> getTopSellingProducts();
}
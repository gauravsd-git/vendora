package com.gaurav.vendora.repository;

import com.gaurav.vendora.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("""
        SELECT oi.product.name, SUM(oi.quantity)
        FROM OrderItem oi
        WHERE oi.order.store.id = :storeId
        GROUP BY oi.product.name
        ORDER BY SUM(oi.quantity) DESC
    """)
    List<Object[]> getTopSellingProductsByStore(@Param("storeId") Long storeId);
}
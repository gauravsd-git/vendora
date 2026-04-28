package com.gaurav.vendora.repository;

import com.gaurav.vendora.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByStoreId(Long storeId);

    Optional<Order> findByIdAndStoreId(Long id, Long storeId);

    @Query("""
        SELECT COALESCE(SUM(o.totalAmount), 0)
        FROM Order o
        WHERE o.store.id = :storeId
    """)
    Double getTotalRevenueByStore(@Param("storeId") Long storeId);

    @Query("""
        SELECT COUNT(o)
        FROM Order o
        WHERE o.store.id = :storeId
    """)
    Long getTotalOrdersByStore(@Param("storeId") Long storeId);
}
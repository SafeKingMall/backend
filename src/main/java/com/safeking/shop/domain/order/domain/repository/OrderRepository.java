package com.safeking.shop.domain.order.domain.repository;

import com.safeking.shop.domain.order.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("select o from Order o" +
            " join fetch o.delivery" +
            " where o.id = :id")
    Optional<Order> findOrder(@Param("id") Long id);
    @Query("select o from Order o" +
            " join fetch o.delivery" +
            " join fetch o.orderItems" +
            " join fetch ")
    Optional<Order> findOrderDetail();
}

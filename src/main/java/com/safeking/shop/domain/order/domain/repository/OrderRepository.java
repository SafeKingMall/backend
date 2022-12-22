package com.safeking.shop.domain.order.domain.repository;

import com.safeking.shop.domain.order.domain.entity.Order;
import com.safeking.shop.domain.order.web.query.repository.OrderRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {
    @Query("select o from Order o" +
            " join fetch o.delivery d" +
            " where o.id = :id")
    Optional<Order> findOrder(@Param("id") Long id);
    @Query("select o from Order o" +
            " join fetch o.delivery d" +
            " join fetch o.orderItems oi" +
            " join fetch o.safeKingPayment p" +
            " join fetch oi.item i" +
            " where o.id = :id")
    Optional<Order> findOrderDetail(@Param("id") Long id);
}

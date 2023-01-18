package com.safeking.shop.domain.order.domain.repository;

import com.safeking.shop.domain.order.domain.entity.Order;
import com.safeking.shop.domain.order.domain.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrder(Order order);

    @Query("select oi from OrderItem oi where oi.order in :orderList")
    List<OrderItem> findByOrderList(@Param("orderList") List<Order> order);
    @Modifying
    @Query("delete from OrderItem oi where oi in :orderItems")
    void deleteByOrderBatch(@Param("orderItems") List<OrderItem> orderItems);

}

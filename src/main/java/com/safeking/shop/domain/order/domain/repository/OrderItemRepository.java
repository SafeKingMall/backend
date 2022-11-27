package com.safeking.shop.domain.order.domain.repository;

import com.safeking.shop.domain.order.domain.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}

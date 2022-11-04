package com.safeking.shop.domain.order.domain.repository;

import com.safeking.shop.domain.order.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}

package com.safeking.shop.domain.order.domain.repository;

import com.safeking.shop.domain.order.domain.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}

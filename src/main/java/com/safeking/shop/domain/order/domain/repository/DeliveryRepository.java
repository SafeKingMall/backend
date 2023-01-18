package com.safeking.shop.domain.order.domain.repository;

import com.safeking.shop.domain.order.domain.entity.Delivery;
import com.safeking.shop.domain.order.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    @Modifying
    @Query("delete from Delivery d where d in :deliveryList")
    void deleteByOrderBatch(@Param("deliveryList") List<Delivery> deliveryList);
}

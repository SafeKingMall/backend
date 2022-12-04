package com.safeking.shop.domain.order.web.query.repository;

import com.safeking.shop.domain.order.domain.entity.Order;
import com.safeking.shop.domain.order.web.dto.request.user.search.OrderSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {
    Page<Order> findOrders(Pageable pageable, OrderSearchCondition condition, Long memberId);
    Page<Order> findOrdersByAdmin(Pageable pageable, OrderSearchCondition condition);
}

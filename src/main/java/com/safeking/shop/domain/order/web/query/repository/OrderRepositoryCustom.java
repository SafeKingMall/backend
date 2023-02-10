package com.safeking.shop.domain.order.web.query.repository;

import com.safeking.shop.domain.order.domain.entity.Order;
import com.safeking.shop.domain.order.web.dto.request.user.search.OrderSearchCondition;
import com.safeking.shop.domain.payment.web.client.dto.request.PaymentSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {
    Page<Order> findOrders(Pageable pageable, OrderSearchCondition condition, Long memberId); // 주문 목록(유저)
    Page<Order> findOrdersByAdmin(Pageable pageable, OrderSearchCondition condition); // 주문 목록(어드민)
    Page<Order> findOrdersByCancel(Pageable pageable, PaymentSearchCondition condition, Long memberId); // 환불 내역
}

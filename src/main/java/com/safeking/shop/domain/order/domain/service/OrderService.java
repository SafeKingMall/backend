package com.safeking.shop.domain.order.domain.service;

import com.safeking.shop.domain.order.domain.entity.Delivery;
import com.safeking.shop.domain.order.domain.entity.Order;
import com.safeking.shop.domain.order.domain.entity.status.OrderStatus;
import com.safeking.shop.domain.order.domain.service.dto.DeliveryCreateDto;
import com.safeking.shop.domain.order.domain.service.dto.OrderOrderDto;
import com.safeking.shop.domain.user.domain.entity.Member;

import java.util.List;

public interface OrderService {
    //Order findOrder(); //주문 단건 조회
    //List<Order> findOrders(); //주문 다건 조회
    void cancel(List<Long> ids); //주문 취소
    Long order(OrderOrderDto orderOrderDto, DeliveryCreateDto deliveryCreateDto); //주문
    Long updateOrder(OrderOrderDto orderOrderDto, DeliveryCreateDto deliveryCreateDto); //주문 정보 수정
}

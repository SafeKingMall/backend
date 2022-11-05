package com.safeking.shop.domain.order.domain.service;

import com.safeking.shop.domain.order.domain.service.dto.DeliveryDto;
import com.safeking.shop.domain.order.domain.service.dto.OrderCancelDto;
import com.safeking.shop.domain.order.domain.service.dto.OrderDto;

import java.util.List;

public interface OrderService {
    //Order findOrder(); //주문 단건 조회
    //List<Order> findOrders(); //주문 다건 조회
    void cancel(OrderCancelDto orderCancelDto); //주문 취소
    Long order(OrderDto orderOrderDto, DeliveryDto deliveryCreateDto); //주문
    Long updateOrder(OrderDto orderOrderDto, DeliveryDto deliveryCreateDto); //주문 정보 수정
}

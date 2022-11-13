package com.safeking.shop.domain.order.domain.service;

import com.safeking.shop.domain.order.web.dto.request.cancel.CancelDto;
import com.safeking.shop.domain.order.domain.service.dto.order.OrderDto;
import com.safeking.shop.domain.order.web.dto.request.modify.ModifyInfoDto;

public interface OrderService {
    //Order findOrder(); //주문 단건 조회
    //List<Order> findOrders(); //주문 다건 조회
    void cancel(CancelDto cancelDto); //주문 취소
    Long order(OrderDto orderDto); //주문
    Long modifyOrder(ModifyInfoDto modifyInfoDto); //주문 정보 수정
}

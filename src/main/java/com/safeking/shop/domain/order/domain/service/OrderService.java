package com.safeking.shop.domain.order.domain.service;

import com.safeking.shop.domain.order.domain.entity.Order;
import com.safeking.shop.domain.order.web.dto.request.cancel.CancelRequest;
import com.safeking.shop.domain.order.web.dto.request.order.OrderRequest;
import com.safeking.shop.domain.order.web.dto.request.modify.ModifyInfoRequest;
import com.safeking.shop.domain.user.domain.entity.member.Member;

public interface OrderService {

    Order findOrder(Long id); //주문 단건 조회
    //List<Order> findOrders(); //주문 다건 조회
    void cancel(CancelRequest cancelRequest); //주문 취소
    Long order(Member member, OrderRequest orderRequest); //주문
    Long modifyOrder(ModifyInfoRequest modifyInfoRequest); //주문 정보 수정
}

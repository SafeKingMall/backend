package com.safeking.shop.domain.order.domain.service;

import com.safeking.shop.domain.order.domain.entity.Order;
import com.safeking.shop.domain.order.web.dto.request.admin.modify.AdminModifyInfoRequest;
import com.safeking.shop.domain.order.web.dto.request.user.cancel.CancelRequest;
import com.safeking.shop.domain.order.web.dto.request.user.order.OrderRequest;
import com.safeking.shop.domain.order.web.dto.request.user.modify.ModifyInfoRequest;
import com.safeking.shop.domain.order.web.dto.request.user.search.OrderSearchCondition;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.siot.IamportRestClient.exception.IamportResponseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface OrderService {

    Order searchOrder(Long id); //주문 단건 조회
    Order searchOrderDetail(Long id); //주문 상세 조회
    Page<Order> searchOrders(Pageable pageable, OrderSearchCondition condition, Long memberId); //주문 다건 조회
    void cancel(CancelRequest cancelRequest); //주문 취소
    Long order(Member member, OrderRequest orderRequest); //주문
    Long modifyOrder(ModifyInfoRequest modifyInfoRequest); //사용자 주문 정보 수정
    Long modifyOrderByAdmin(AdminModifyInfoRequest modifyInfoRequest, Long orderId); //관리자 주문 정보 수정
    Page<Order> searchOrdersByAdmin(Pageable pageable, OrderSearchCondition condition);
    void delete(Member member);
    void deleteByMemberBatch(Member member);
}

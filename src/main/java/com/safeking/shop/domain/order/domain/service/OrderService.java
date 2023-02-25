package com.safeking.shop.domain.order.domain.service;

import com.safeking.shop.domain.order.web.dto.request.admin.modify.AdminModifyInfoRequest;
import com.safeking.shop.domain.order.web.dto.request.user.cancel.CancelRequest;
import com.safeking.shop.domain.order.web.dto.request.user.order.OrderRequest;
import com.safeking.shop.domain.order.web.dto.request.user.modify.ModifyInfoRequest;
import com.safeking.shop.domain.order.web.dto.request.user.search.OrderSearchCondition;
import com.safeking.shop.domain.order.web.dto.response.admin.orderdetail.AdminOrderDetailResponse;
import com.safeking.shop.domain.order.web.dto.response.admin.search.AdminOrderListResponse;
import com.safeking.shop.domain.order.web.dto.response.user.order.OrderResponse;
import com.safeking.shop.domain.order.web.dto.response.user.orderdetail.OrderDetailResponse;
import com.safeking.shop.domain.order.web.dto.response.user.orderinfo.OrderInfoResponse;
import com.safeking.shop.domain.order.web.dto.response.user.search.OrderListResponse;
import com.safeking.shop.domain.payment.web.client.dto.request.PaymentSearchCondition;
import com.safeking.shop.domain.payment.web.client.dto.response.askcancel.PaymentAskCancelResponse;
import com.safeking.shop.domain.payment.web.client.dto.response.canceldetail.PaymentCancelDetailResponse;
import com.safeking.shop.domain.payment.web.client.dto.response.cancellist.PaymentCancelListResponse;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    OrderInfoResponse searchOrder(Long id, Long memberId); //주문 단건 조회
    OrderDetailResponse searchOrderDetailByUser(Long id, Long memberId); //주문 상세 조회(유저)
    AdminOrderDetailResponse searchOrderDetailByAdmin(Long id); //주문 상세 조회(어드민)
    OrderListResponse searchOrders(Pageable pageable, OrderSearchCondition condition, Long memberId); //주문 다건 조회
    void cancel(CancelRequest cancelRequest); //주문 취소
    OrderResponse order(Member member, OrderRequest orderRequest); //주문
    Long modifyOrder(ModifyInfoRequest modifyInfoRequest); //사용자 주문 정보 수정
    Long modifyOrderByAdmin(AdminModifyInfoRequest modifyInfoRequest, Long orderId); //관리자 주문 정보 수정
    AdminOrderListResponse searchOrdersByAdmin(Pageable pageable, OrderSearchCondition condition); // 주문관리 목록
    PaymentCancelListResponse searchPaymentsByCancel(Pageable pageable, PaymentSearchCondition condition, Long memberId); // 환불내역
    void delete(Member member);
    void deleteByMemberBatch(Member member);
    PaymentAskCancelResponse searchPaymentByCancel(Long orderId, Long memberId); // 환불신청 단건 조회
    PaymentCancelDetailResponse searchPaymentCancelDetailByUser(Long orderId, Long memberId); // 환불 상세내역
}

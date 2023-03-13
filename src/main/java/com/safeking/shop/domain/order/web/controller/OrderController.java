package com.safeking.shop.domain.order.web.controller;

import com.safeking.shop.domain.order.domain.entity.Order;
import com.safeking.shop.domain.order.domain.service.OrderService;
import com.safeking.shop.domain.order.web.dto.request.user.order.OrderRequest;
import com.safeking.shop.domain.order.web.dto.request.user.cancel.CancelRequest;
import com.safeking.shop.domain.order.web.dto.request.user.modify.ModifyInfoRequest;
import com.safeking.shop.domain.order.web.dto.request.user.search.OrderSearchCondition;
import com.safeking.shop.domain.order.web.dto.response.OrderBasicResponse;
import com.safeking.shop.domain.order.web.dto.response.user.order.OrderResponse;
import com.safeking.shop.domain.order.web.dto.response.user.orderinfo.OrderInfoResponse;
import com.safeking.shop.domain.order.web.dto.response.user.orderinfo.OrderInfoDeliveryResponse;
import com.safeking.shop.domain.order.web.dto.response.user.orderinfo.OrderInfoOrderResponse;
import com.safeking.shop.domain.order.web.dto.response.user.orderdetail.*;
import com.safeking.shop.domain.order.web.dto.response.user.orderitems.OrderItemsResponse;
import com.safeking.shop.domain.order.web.dto.response.user.search.*;
import com.safeking.shop.domain.order.web.query.service.ValidationOrderService;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.siot.IamportRestClient.IamportClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.ArrayList;
import java.util.List;

import static com.safeking.shop.domain.order.constant.OrderConst.*;
import static com.safeking.shop.global.jwt.TokenUtils.AUTH_HEADER;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class OrderController {
    private final OrderService orderService;
    private final ValidationOrderService validationOrderService;


    /**
     * 주문
     */
    @PostMapping("/order")
    public ResponseEntity<OrderResponse> order(@Valid @RequestBody OrderRequest orderRequest, HttpServletRequest request) {

        //회원 검증
        Member member = validationOrderService.validationMember(request.getHeader(AUTH_HEADER));

        //주문
        OrderResponse orderResponse = orderService.order(member, orderRequest);

        return new ResponseEntity<>(orderResponse, OK);
    }

    /**
     * 주문 취소
     */
//    @PatchMapping("/order")
//    public ResponseEntity<OrderBasicResponse> cancel(@Valid @RequestBody CancelRequest cancelRequest, HttpServletRequest request) {
//
//        //회원 검증
//        validationOrderService.validationMember(request.getHeader(AUTH_HEADER));
//
//        //주문 취소
//        orderService.cancel(cancelRequest);
//
//        return new ResponseEntity<>(new OrderBasicResponse(ORDER_CANCEL_SUCCESS), OK);
//    }

    /**
     * 주문(배송) 정보 수정
     */
    @PutMapping("/order")
    public ResponseEntity<OrderBasicResponse> modify(@Valid @RequestBody ModifyInfoRequest modifyInfoRequest, HttpServletRequest request) {

        //회원 검증
        validationOrderService.validationMember(request.getHeader(AUTH_HEADER));

        //주문(배송) 정보 수정
        orderService.modifyOrder(modifyInfoRequest);

        return new ResponseEntity<>(new OrderBasicResponse(ORDER_MODIFY_SUCCESS), OK);
    }

    /**
     * 주문(배송) 정보 조회
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<OrderInfoResponse> searchOrder(@PathVariable Long orderId, HttpServletRequest request) {

        //회원 검증
        Member member = validationOrderService.validationMember(request.getHeader(AUTH_HEADER));

        //주문(배송) 정보 조회(단건)
        return new ResponseEntity<>(orderService.searchOrder(orderId, member.getId()), OK);
    }

    /**
     * 주문 상세 조회
     */
    @GetMapping("/order/detail/{orderId}")
    public ResponseEntity<OrderDetailResponse> searchOrderDetail(@PathVariable Long orderId, HttpServletRequest request) {
        // 회원 검증
        Member member = validationOrderService.validationMember(request.getHeader(AUTH_HEADER));

        // 주문 상세 조회
        return new ResponseEntity<>(orderService.searchOrderDetailByUser(orderId, member.getId()), OK);
    }

    /**
     * 주문 목록 조회
     */
    @GetMapping("/order/list")
    public ResponseEntity<OrderListResponse> searchOrderList(OrderSearchCondition condition, Pageable pageable, HttpServletRequest request) {
        Member member = validationOrderService.validationMember(request.getHeader(AUTH_HEADER));

        //주문 다건 조회
        return new ResponseEntity<>(orderService.searchOrders(pageable, condition, member.getId()), OK);
    }

    /**
     * 주문 상품 id 목록 조회
     */
    @GetMapping("/order/orderItemIds/{merchantUid}")
    public ResponseEntity<OrderItemsResponse> searchOrderItemIds(@PathVariable String merchantUid, HttpServletRequest request) {
        Member member = validationOrderService.validationMember(request.getHeader(AUTH_HEADER));

        return new ResponseEntity<>(orderService.searchOrderItemIds(merchantUid, member.getId()), OK);
    }

}

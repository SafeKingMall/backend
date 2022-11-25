package com.safeking.shop.domain.order.web.controller;

import com.safeking.shop.domain.order.domain.entity.Order;
import com.safeking.shop.domain.order.domain.service.OrderService;
import com.safeking.shop.domain.order.web.dto.request.order.OrderRequest;
import com.safeking.shop.domain.order.web.dto.request.cancel.CancelRequest;
import com.safeking.shop.domain.order.web.dto.request.modify.ModifyInfoRequest;
import com.safeking.shop.domain.order.web.dto.response.OrderBasicResponse;
import com.safeking.shop.domain.order.web.dto.response.order.OrderResponse;
import com.safeking.shop.domain.order.web.dto.response.order.OrderDeliveryResponse;
import com.safeking.shop.domain.order.web.dto.response.order.OrderOrderResponse;
import com.safeking.shop.domain.order.web.dto.response.orderdetail.*;
import com.safeking.shop.domain.order.web.query.service.ValidationOrderService;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

import static com.safeking.shop.domain.order.web.OrderConst.*;
import static com.safeking.shop.global.jwt.TokenUtils.AUTH_HEADER;
import static com.safeking.shop.global.jwt.TokenUtils.BEARER;
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
    public ResponseEntity<OrderBasicResponse> order(@Valid @RequestBody OrderRequest orderRequest, HttpServletRequest request) {

        //회원 검증
        Member member = validationOrderService.validationMember(request.getHeader(AUTH_HEADER).replace(BEARER, ""));

        //주문
        orderService.order(member, orderRequest);

        return new ResponseEntity<>(new OrderBasicResponse(ORDER_SUCCESS), OK);
    }

    /**
     * 주문 취소
     */
    @PatchMapping("/order")
    public ResponseEntity<OrderBasicResponse> cancel(@Valid @RequestBody CancelRequest cancelRequest, HttpServletRequest request) {

        //회원 검증
        validationOrderService.validationMember(request.getHeader(AUTH_HEADER));

        //주문 취소
        orderService.cancel(cancelRequest);

        return new ResponseEntity<>(new OrderBasicResponse(ORDER_CANCEL_SUCCESS), OK);
    }

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
    public ResponseEntity<OrderResponse> find(@PathVariable Long orderId, HttpServletRequest request) {

        //회원 검증
        validationOrderService.validationMember(request.getHeader(AUTH_HEADER));

        //주문(배송) 정보 조회(단건)
        Order findOrder = orderService.searchOrder(orderId);

        return new ResponseEntity<>(getOrderResponse(findOrder), OK);
    }

    private OrderResponse getOrderResponse(Order findOrder) {

        //주문(배송) 조회 응답 데이터
        OrderDeliveryResponse delivery = OrderDeliveryResponse.builder()
                .receiver(findOrder.getDelivery().getReceiver())
                .address(findOrder.getDelivery().getAddress())
                .phoneNumber(findOrder.getDelivery().getPhoneNumber())
                .memo(findOrder.getDelivery().getMemo())
                .build();

        OrderOrderResponse order = OrderOrderResponse.builder()
                .id(findOrder.getId())
                .memo(findOrder.getMemo())
                .build();

        OrderResponse orderResponse = OrderResponse.builder()
                .message(ORDER_FIND_SUCCESS)
                .order(order)
                .delivery(delivery)
                .build();

        return orderResponse;
    }

    /**
     * 주문 상세 조회
     */
    @GetMapping("/order/detail/{orderId}")
    public ResponseEntity<OrderDetailResponse> findDetail(@PathVariable Long orderId, HttpServletRequest request) {
        // 회원 검증
        validationOrderService.validationMember(request.getHeader(AUTH_HEADER));

        // 주문 상세 조회
        Order findOrderDetail = orderService.searchOrderDetail(orderId);

        return new ResponseEntity<>(getOrderDetailResponse(findOrderDetail), OK);
    }

    private OrderDetailResponse getOrderDetailResponse(Order findOrderDetail) {

        // 주문 상세 조회 응답 데이터
        List<OrderDetailItem> orderItems = findOrderDetail.getOrderItems().stream()
                .map(oi -> OrderDetailItem.builder()
                        .id(oi.getId())
                        .name(oi.getItem().getName())
                        .count(oi.getCount())
                        .price(oi.getOrderPrice())
                        .build())
                .collect(Collectors.toList());

        OrderDetailDelivery delivery = OrderDetailDelivery.builder()
                .id(findOrderDetail.getDelivery().getId())
                .status(findOrderDetail.getDelivery().getStatus().getDescription())
                .receiver(findOrderDetail.getDelivery().getReceiver())
                .phoneNumber(findOrderDetail.getDelivery().getPhoneNumber())
                .address(findOrderDetail.getDelivery().getAddress())
                .memo(findOrderDetail.getDelivery().getMemo())
                .build();

        OrderDetailPayment payment = OrderDetailPayment.builder()
                .status(findOrderDetail.getPayment().getStatus().getDescription())
                .build();

        OrderDetailOrderResponse order = OrderDetailOrderResponse.builder()
                .id(findOrderDetail.getId())
                .status(findOrderDetail.getStatus().getDescription())
                .price(findOrderDetail.getPayment().getPrice())
                .memo(findOrderDetail.getMemo())
                .date(findOrderDetail.getCreateDate().toString())
                .orderItems(orderItems)
                .payment(payment)
                .delivery(delivery)
                .build();

        OrderDetailResponse orderDetailResponse = OrderDetailResponse.builder()
                .message(ORDER_DETAIL_FIND_SUCCESS)
                .order(order)
                .build();

        return orderDetailResponse;
    }
}

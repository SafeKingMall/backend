package com.safeking.shop.domain.order.web.controller;

import com.safeking.shop.domain.order.domain.entity.Order;
import com.safeking.shop.domain.order.domain.service.OrderService;
import com.safeking.shop.domain.order.web.dto.request.user.order.OrderRequest;
import com.safeking.shop.domain.order.web.dto.request.user.cancel.CancelRequest;
import com.safeking.shop.domain.order.web.dto.request.user.modify.ModifyInfoRequest;
import com.safeking.shop.domain.order.web.dto.request.user.search.OrderSearchCondition;
import com.safeking.shop.domain.order.web.dto.response.OrderBasicResponse;
import com.safeking.shop.domain.order.web.dto.response.user.order.OrderResponse;
import com.safeking.shop.domain.order.web.dto.response.user.order.OrderDeliveryResponse;
import com.safeking.shop.domain.order.web.dto.response.user.order.OrderOrderResponse;
import com.safeking.shop.domain.order.web.dto.response.user.orderdetail.*;
import com.safeking.shop.domain.order.web.dto.response.user.search.OrderListOrderItemResponse;
import com.safeking.shop.domain.order.web.dto.response.user.search.OrderListOrdersResponse;
import com.safeking.shop.domain.order.web.dto.response.user.search.OrderListPaymentResponse;
import com.safeking.shop.domain.order.web.dto.response.user.search.OrderListResponse;
import com.safeking.shop.domain.order.web.query.service.ValidationOrderService;
import com.safeking.shop.domain.user.domain.entity.member.Member;
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
import java.util.stream.Collectors;

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
    public ResponseEntity<OrderBasicResponse> order(@Valid @RequestBody OrderRequest orderRequest, HttpServletRequest request) {

        //회원 검증
        Member member = validationOrderService.validationMember(request.getHeader(AUTH_HEADER));

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
    public ResponseEntity<OrderResponse> searchOrder(@PathVariable Long orderId, HttpServletRequest request) {

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
                .merchantUid(findOrder.getMerchantUid())
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
    public ResponseEntity<OrderDetailResponse> searchOrderDetail(@PathVariable Long orderId, HttpServletRequest request) {
        // 회원 검증
        validationOrderService.validationMember(request.getHeader(AUTH_HEADER));

        // 주문 상세 조회
        return new ResponseEntity<>(orderService.searchOrderDetailByUser(orderId), OK);
    }

    /**
     * 주문 다건 조회
     */
    @GetMapping("/order/list")
    public ResponseEntity<OrderListResponse> searchOrderList(OrderSearchCondition condition, Pageable pageable, HttpServletRequest request) {
        Member member = validationOrderService.validationMember(request.getHeader(AUTH_HEADER));

        //주문 다건 조회
        Page<Order> ordersPage = orderService.searchOrders(pageable, condition, member.getId());

        OrderListResponse orderListResponse = getOrderListResponse(ordersPage);

        return new ResponseEntity<>(orderListResponse, OK);
    }

    private static OrderListResponse getOrderListResponse(Page<Order> ordersPage) {

        List<Order> findOrders = ordersPage.getContent();
        List<OrderListOrdersResponse> orders = new ArrayList<>();

        for(Order o : findOrders) {
            OrderListPaymentResponse payment = OrderListPaymentResponse.builder()
                    .status(o.getSafeKingPayment().getStatus().getDescription())
                    .build();

            OrderListOrderItemResponse orderItem = OrderListOrderItemResponse.builder()
                    .id(o.getOrderItems().get(0).getItem().getId())
                    .name(o.getOrderItems().get(0).getItem().getName())
                    .build();

            OrderListOrdersResponse order = OrderListOrdersResponse.builder()
                    .id(o.getId())
                    .merchantUid(o.getMerchantUid())
                    .status(o.getStatus().getDescription())
                    .price(o.getSafeKingPayment().getAmount())
                    .date(o.getCreateDate())
                    .count(o.getOrderItems().size())
                    .orderItem(orderItem)
                    .payment(payment)
                    .build();

            orders.add(order);
        }

        return OrderListResponse.builder()
                .message(ORDER_LIST_FIND_SUCCESS)
                .orders(orders)
                .totalElements(ordersPage.getTotalElements())
                .totalPages(ordersPage.getTotalPages())
                .size(ordersPage.getSize())
                .build();
    }
}

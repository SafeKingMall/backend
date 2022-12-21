package com.safeking.shop.domain.order.web.controller;

import com.safeking.shop.domain.order.domain.entity.Order;
import com.safeking.shop.domain.order.domain.service.OrderService;
import com.safeking.shop.domain.order.web.dto.request.admin.modify.AdminModifyInfoRequest;
import com.safeking.shop.domain.order.web.dto.request.user.search.OrderSearchCondition;
import com.safeking.shop.domain.order.web.dto.response.OrderBasicResponse;
import com.safeking.shop.domain.order.web.dto.response.admin.orderdetail.*;
import com.safeking.shop.domain.order.web.dto.response.admin.search.*;
import com.safeking.shop.domain.order.web.query.service.ValidationOrderService;
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
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class OrderAdminController {

    private final OrderService orderService;
    private final ValidationOrderService validationOrderService;

    @GetMapping("/order/detail/{orderId}")
    public ResponseEntity<AdminOrderDetailResponse> searchOrderDetailByAdmin(@PathVariable("orderId") Long orderId,
                                                                             HttpServletRequest request) {

        // 회원 검증
        validationOrderService.validationMember(request.getHeader(AUTH_HEADER));

        // 주문 상세 조회
        Order findOrderDetail = orderService.searchOrderDetail(orderId);

        return new ResponseEntity<>(getOrderDetailResponse(findOrderDetail), OK);
    }

    private AdminOrderDetailResponse getOrderDetailResponse(Order findOrderDetail) {

        // 주문 상세 조회 응답 데이터
        List<AdminOrderDetailOrderItemsResponse> orderItems = findOrderDetail.getOrderItems().stream()
                .map(oi -> AdminOrderDetailOrderItemsResponse.builder()
                        .id(oi.getId())
                        .name(oi.getItem().getName())
                        .count(oi.getCount())
                        .price(oi.getOrderPrice())
                        .build())
                .collect(Collectors.toList());

        /**
         * 추후, 결제 API에서 데이터 수집해야함.
         */
        AdminOrderDetailDeliveryResponse delivery = AdminOrderDetailDeliveryResponse.builder()
                .id(findOrderDetail.getDelivery().getId())
                .status(findOrderDetail.getDelivery().getStatus().getDescription())
                .receiver(findOrderDetail.getDelivery().getReceiver())
                .phoneNumber(findOrderDetail.getDelivery().getPhoneNumber())
                .address(findOrderDetail.getDelivery().getAddress())
                .memo(findOrderDetail.getDelivery().getMemo())
                .invoiceNumber(findOrderDetail.getDelivery().getInvoiceNumber())
                .cost(findOrderDetail.getDelivery().getCost())
                .company(findOrderDetail.getDelivery().getCompany())
                .build();

        /**
         * 추후, 결제 API에서 데이터 수집해야함.
         */
        AdminOrderDetailPaymentResponse payment = AdminOrderDetailPaymentResponse.builder()
                .status(findOrderDetail.getPayment().getStatus().getDescription())
                .company(findOrderDetail.getPayment().getCompany())
                .means(findOrderDetail.getPayment().getMeans())
                .businessNumber(findOrderDetail.getPayment().getBusinessNumber())
                .price(findOrderDetail.getPayment().getPrice())
                .build();

        AdminOrderDetailOrderResponse order = AdminOrderDetailOrderResponse.builder()
                .id(findOrderDetail.getId())
                .status(findOrderDetail.getStatus().getDescription())
                .price(findOrderDetail.getPayment().getPrice())
                .memo(findOrderDetail.getMemo())
                .date(findOrderDetail.getCreateDate())
                .adminMemo(findOrderDetail.getAdminMemo())
                .orderItems(orderItems)
                .payment(payment)
                .delivery(delivery)
                .build();

        AdminOrderDetailResponse adminOrderDetailResponse = AdminOrderDetailResponse.builder()
                .message(ADMIN_ORDER_DETAIL_FIND_SUCCESS)
                .order(order)
                .build();

        return adminOrderDetailResponse;
    }

    /**
     * 주문 관리 상세 수정
     */
    @PutMapping("/order/detail/{orderId}")
    public ResponseEntity<OrderBasicResponse> modifyByAdmin(@PathVariable("orderId") Long orderId,
                                                            @Valid @RequestBody AdminModifyInfoRequest adminModifyInfoRequest,
                                                            HttpServletRequest request) {
        // 회원 검증
        validationOrderService.validationMember(request.getHeader(AUTH_HEADER));

        // 주문관리 상세 수정
        orderService.modifyOrderByAdmin(adminModifyInfoRequest, orderId);

        return new ResponseEntity<>(new OrderBasicResponse(ADMIN_ORDER_DETAIL_MODIFY_SUCCESS), OK);
    }

    /**
     * 주문관리 목록 조회
     */
    @GetMapping("/order/list")
    public ResponseEntity<AdminOrderListResponse> search(OrderSearchCondition condition,
                                                         Pageable pageable,
                                                         HttpServletRequest request) {
        // 회원 검증
        validationOrderService.validationMember(request.getHeader(AUTH_HEADER));

        // 주문관리 목록 조회
        Page<Order> ordersPage = orderService.searchOrdersByAdmin(pageable, condition);
        List<Order> findOrders = ordersPage.getContent();

        AdminOrderListResponse adminOrderListResponse = getAdminOrderListResponse(findOrders);

        return new ResponseEntity<>(adminOrderListResponse, OK);
    }

    private static AdminOrderListResponse getAdminOrderListResponse(List<Order> findOrders) {

        List<AdminOrderListOrderResponse> orders = new ArrayList<>();

        for(Order o : findOrders) {
            AdminOrderListOrderItemResponse orderItem = AdminOrderListOrderItemResponse.builder()
                    .id(o.getOrderItems().get(0).getItem().getId())
                    .name(o.getOrderItems().get(0).getItem().getName())
                    .build();

            AdminOrderListPaymentResponse payment = AdminOrderListPaymentResponse.builder()
                    .status(o.getPayment().getStatus().getDescription())
                    .build();

            AdminOrderListMemberResponse member = AdminOrderListMemberResponse.builder()
                    .name(o.getMember().getName())
                    .build();

            AdminOrderListDeliveryResponse delivery = AdminOrderListDeliveryResponse.builder()
                    .receiver(o.getDelivery().getReceiver())
                    .status(o.getDelivery().getStatus().getDescription())
                    .build();

            AdminOrderListOrderResponse order = AdminOrderListOrderResponse.builder()
                    .id(o.getId())
                    .status(o.getStatus().getDescription())
                    .price(o.getPayment().getPrice())
                    .date(o.getCreateDate())
                    .orderItemCount(o.getOrderItems().size())
                    .orderItem(orderItem)
                    .payment(payment)
                    .member(member)
                    .delivery(delivery)
                    .build();

            orders.add(order);
        }

        return AdminOrderListResponse.builder()
                .message(ADMIN_ORDER_LIST_FIND_SUCCESS)
                .orders(orders)
                .build();
    }

}

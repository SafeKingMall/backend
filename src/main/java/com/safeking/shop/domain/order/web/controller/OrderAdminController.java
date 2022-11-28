package com.safeking.shop.domain.order.web.controller;

import com.safeking.shop.domain.order.domain.entity.Order;
import com.safeking.shop.domain.order.domain.service.OrderService;
import com.safeking.shop.domain.order.web.dto.response.admin.orderdetail.*;
import com.safeking.shop.domain.order.web.dto.response.user.order.OrderResponse;
import com.safeking.shop.domain.order.web.dto.response.user.orderdetail.*;
import com.safeking.shop.domain.order.web.query.service.ValidationOrderService;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.auth.AUTH;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.stream.Collectors;

import static com.safeking.shop.domain.order.web.OrderConst.ADMIN_ORDER_DETAIL_FIND_SUCCESS;
import static com.safeking.shop.domain.order.web.OrderConst.ORDER_DETAIL_FIND_SUCCESS;
import static com.safeking.shop.global.jwt.TokenUtils.AUTH_HEADER;
import static org.apache.naming.ResourceRef.AUTH;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class OrderAdminController {

    private final OrderService orderService;
    private final ValidationOrderService validationOrderService;

    @GetMapping("/order/detail/{orderId}")
    public ResponseEntity<AdminOrderDetailResponse> searchOrderDetail(@PathVariable("orderId") Long orderId, HttpServletRequest request) {

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
                .adminMemo(findOrderDetail.getMemo())
                .date(findOrderDetail.getCreateDate().toString())
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
}

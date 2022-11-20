package com.safeking.shop.domain.order.web.controller;

import com.safeking.shop.domain.order.domain.entity.Order;
import com.safeking.shop.domain.order.domain.service.OrderService;
import com.safeking.shop.domain.order.web.dto.request.order.OrderRequest;
import com.safeking.shop.domain.order.web.dto.request.cancel.CancelRequest;
import com.safeking.shop.domain.order.web.dto.request.modify.ModifyInfoRequest;
import com.safeking.shop.domain.order.web.dto.response.OrderBasicResponse;
import com.safeking.shop.domain.order.web.dto.response.findorder.FindOrderResponse;
import com.safeking.shop.domain.order.web.dto.response.findorder.OrderDeliveryResponse;
import com.safeking.shop.domain.order.web.dto.response.findorder.OrderResponse;
import com.safeking.shop.domain.order.web.query.service.ValidationOrderService;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.safeking.shop.domain.order.web.OrderConst.*;
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
    public ResponseEntity<FindOrderResponse> find(@PathVariable Long orderId, HttpServletRequest request) {

        //회원 검증
        validationOrderService.validationMember(request.getHeader(AUTH_HEADER));

        //주문(배송) 정보 조회(단건)
        Order findOrder = orderService.findOrder(orderId);

        //응답 데이터
        OrderDeliveryResponse delivery = OrderDeliveryResponse.builder()
                .receiver(findOrder.getDelivery().getReceiver())
                .address(findOrder.getDelivery().getAddress())
                .phoneNumber(findOrder.getDelivery().getPhoneNumber())
                .memo(findOrder.getDelivery().getMemo())
                .build();

        OrderResponse order = OrderResponse.builder()
                .id(findOrder.getId())
                .memo(findOrder.getMemo())
                .build();

        FindOrderResponse orderResponse = FindOrderResponse.builder()
                .message(ORDER_FIND_SUCCESS)
                .order(order)
                .delivery(delivery)
                .build();

        return new ResponseEntity<>(orderResponse, OK);
    }
}

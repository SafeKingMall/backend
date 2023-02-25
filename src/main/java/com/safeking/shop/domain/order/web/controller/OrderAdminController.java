package com.safeking.shop.domain.order.web.controller;

import com.safeking.shop.domain.order.domain.repository.OrderRepository;
import com.safeking.shop.domain.order.domain.service.OrderService;
import com.safeking.shop.domain.order.web.dto.request.admin.modify.AdminModifyInfoRequest;
import com.safeking.shop.domain.order.web.dto.request.user.search.OrderSearchCondition;
import com.safeking.shop.domain.order.web.dto.response.OrderBasicResponse;
import com.safeking.shop.domain.order.web.dto.response.admin.orderdetail.*;
import com.safeking.shop.domain.order.web.query.service.ValidationOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
    private final OrderRepository orderRepository;
    /**
     * 관리자 주문 상세 조회
     */
    @GetMapping("/order/detail/{orderId}")
    public ResponseEntity<AdminOrderDetailResponse> searchOrderDetailByAdmin(@PathVariable("orderId") Long orderId,
                                                                             HttpServletRequest request) {

        // 회원 검증
        validationOrderService.validationMember(request.getHeader(AUTH_HEADER));

        // 주문 상세 조회
        return new ResponseEntity<>(orderService.searchOrderDetailByAdmin(orderId), OK);
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
    public ResponseEntity<?> search(OrderSearchCondition condition,
                                                         Pageable pageable,
                                                         HttpServletRequest request) {
        // 회원 검증
        validationOrderService.validationMember(request.getHeader(AUTH_HEADER));

        //return new ResponseEntity<>(orderRepository.findOrdersByAdmin(pageable, condition), OK);

        // 주문관리 목록 조회
        return new ResponseEntity<>(orderService.searchOrdersByAdmin(pageable, condition), OK);
    }

}

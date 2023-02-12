package com.safeking.shop.domain.payment.web.controller;

import com.safeking.shop.domain.order.domain.service.OrderService;
import com.safeking.shop.domain.order.web.query.service.ValidationOrderService;
import com.safeking.shop.domain.payment.web.client.dto.request.*;
import com.safeking.shop.domain.payment.web.client.dto.response.PaymentCancelPaymentResponse;
import com.safeking.shop.domain.payment.web.client.dto.response.PaymentCancelResponse;
import com.safeking.shop.domain.payment.web.client.dto.response.PaymentResponse;
import com.safeking.shop.domain.payment.web.client.dto.response.PaymentCallbackResponse;
import com.safeking.shop.domain.payment.web.client.dto.response.askcancel.PaymentAskCancelResponse;
import com.safeking.shop.domain.payment.web.client.dto.response.canceldetail.PaymentCancelDetailResponse;
import com.safeking.shop.domain.payment.web.client.dto.response.cancellist.PaymentCancelListResponse;
import com.safeking.shop.domain.payment.web.client.service.IamportService;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.safeking.shop.global.jwt.TokenUtils.AUTH_HEADER;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class PaymentController {
    private final IamportService iamportService;
    private final OrderService orderService;
    private final ValidationOrderService validationOrderService;

    /**
     * 결제 인증 취소(결제 취소 상태임)
     */
    @PostMapping("/payment/cancel/auth")
    public ResponseEntity<String> cancelPaymentAuth(@Valid @RequestBody PaymentAuthCancelRequest paymentAuthCancelRequest, HttpServletRequest request) {
        validationOrderService.validationMember(request.getHeader(AUTH_HEADER));

        return new ResponseEntity<>(iamportService.authCancel(paymentAuthCancelRequest), OK);
    }

    /**
     * 콜백으로 결제 구현(결제 완료 상태임)
     */
    @PostMapping("/payment")
    public ResponseEntity<PaymentResponse> payment(@Valid @RequestBody PaymentCallbackRequest paymentCallbackRequest, HttpServletRequest request) {

        // 회원 검증
        validationOrderService.validationMember(request.getHeader(AUTH_HEADER));

        PaymentResponse<PaymentCallbackResponse> response = iamportService.paymentByCallback(paymentCallbackRequest);

        return new ResponseEntity<>(response, OK);
    }

    /**
     * 아임포트 웹훅 사용
     * curl -H "Content-Type: application/json" -X POST -d '{ "imp_uid": "imp_1234567890", "merchant_uid": "order_id_8237352", "status": "paid" }' { NotificationURL }
     */
    @PostMapping("/payment/webhook")
    public ResponseEntity<?> paymentWebhook(@Valid @RequestBody PaymentWebhookRequest request) {

        iamportService.paymentByWebhook(request);

        return new ResponseEntity<>(OK);
    }

    /**
     * 결제 취소
     */
    @PostMapping("/payment/cancel")
    public ResponseEntity<PaymentCancelResponse> cancelPayment(@Valid @RequestBody PaymentCancelRequest paymentCancelRequest, HttpServletRequest request) {

        // 회원 검증
        validationOrderService.validationMember(request.getHeader(AUTH_HEADER));

        // 결제 취소
        PaymentCancelResponse response = iamportService.cancel(
                paymentCancelRequest.getImpUid(),
                paymentCancelRequest.getMerchantUid(),
                paymentCancelRequest.getReason(),
                paymentCancelRequest.getReturnFee(),
                iamportService.getSafekingPayment(paymentCancelRequest.getMerchantUid())
        );

        return new ResponseEntity<>(response, OK);
    }

    /**
     * 환불 목록 조회
     */
    @GetMapping("/payment/cancel/list")
    public ResponseEntity<PaymentCancelListResponse> cancelPaymentList(PaymentSearchCondition condition, Pageable pageable, HttpServletRequest request) {

        // 회원 검증
        Member member = validationOrderService.validationMember(request.getHeader(AUTH_HEADER));

        // 환불 내역 조회
        PaymentCancelListResponse response = orderService.searchPaymentsByCancel(pageable, condition, member.getId());

        return new ResponseEntity<>(response, OK);
    }

    /**
     * 환불신청 단건 조회
     */
    @GetMapping("/payment/cancel/ask/{orderId}")
    public ResponseEntity<PaymentAskCancelResponse> askCancelPayment(@PathVariable Long orderId, HttpServletRequest request) {
        // 회원 검증
        Member member = validationOrderService.validationMember(request.getHeader(AUTH_HEADER));

        // 환불신청 단건 조회
        PaymentAskCancelResponse response = orderService.searchPaymentByCancel(orderId, member.getId());

        return new ResponseEntity<>(response, OK);
    }

    /**
     * 환불 상세 내역
     */
    @GetMapping("/payment/cancel/detail/{orderId}")
    public ResponseEntity<PaymentCancelDetailResponse> cancelPaymentDetail(@PathVariable Long orderId, HttpServletRequest request) {

        // 회원 검증
        Member member = validationOrderService.validationMember(request.getHeader(AUTH_HEADER));

        // 환불 상세 내역 조회
        PaymentCancelDetailResponse response = orderService.searchPaymentCancelDetailByUser(orderId, member.getId());

        return new ResponseEntity<>(response, OK);
    }
}

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
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.io.IOException;

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
    private final IamportClient client;


    /**
     * 결제 인증 취소(결제 취소 상태임)
     */
    @PostMapping("/payment/cancel/auth")
    public ResponseEntity<String> cancelPaymentAuth(@Valid @RequestBody PaymentAuthCancelRequest paymentAuthCancelRequest, HttpServletRequest request) {
        validationOrderService.validationMember(request.getHeader(AUTH_HEADER));

        return new ResponseEntity<>(iamportService.authCancel(paymentAuthCancelRequest), OK);
    }

    @GetMapping("/payment/test/{imp}")
    public String test(@PathVariable String imp) {
        StringBuilder sb = new StringBuilder();

        IamportResponse<Payment> paymentIamportResponse;
        try {
            paymentIamportResponse= client.paymentByImpUid(imp);
        } catch (IamportResponseException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        sb.append("code = ").append(paymentIamportResponse.getCode()).append('\n');
        sb.append("message = ").append(paymentIamportResponse.getMessage()).append('\n');
        Payment response = paymentIamportResponse.getResponse();
        if (response.getStatus() != null) sb.append("getStatus = ").append(response.getStatus()).append('\n');
        if (response.getMerchantUid() != null) sb.append("getMerchantUid = ").append(response.getMerchantUid()).append('\n');
        if (response.getCustomerUid() != null) sb.append("getCustomerUid = ").append(response.getCustomerUid()).append('\n');
        if (response.getAmount() != null) sb.append("getAmount = ").append(response.getAmount()).append('\n');
        if (response.getApplyNum() != null) sb.append("getApplyNum = ").append(response.getApplyNum()).append('\n');


        return sb.toString();
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
     *
     * 포트원 웹훅(webhook)은 다음과 같은 경우에 호출됩니다.
     * 결제가 승인되었을 때(모든 결제 수단) - (status : paid)
     * 가상계좌가 발급되었을 때 - (status : ready)
     * 가상계좌에 결제 금액이 입금되었을 때 - (status : paid)
     * 예약결제가 시도되었을 때 - (status : paid or failed)
     * 관리자 콘솔에서 결제 취소되었을 때 - (status : cancelled)
     */
    @PostMapping("/payment/webhook")
    public ResponseEntity<String> paymentWebhook(@Valid @RequestBody PaymentWebhookRequest request) {

        iamportService.paymentByWebhook(request);

        return new ResponseEntity<>("웹훅 이벤트 수신 완료!", OK);
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
                getCancelReason(paymentCancelRequest),
                paymentCancelRequest.getReturnFee()
        );

        return new ResponseEntity<>(response, OK);
    }

    private String getCancelReason(PaymentCancelRequest paymentCancelRequest) {
        if(StringUtils.hasText(paymentCancelRequest.getCustomCancelReason())) {
            return paymentCancelRequest.getCustomCancelReason();
        } else if(StringUtils.hasText(paymentCancelRequest.getConstantCancelReason())) {
            return paymentCancelRequest.getConstantCancelReason();
        }
        return "취소사유 미기재";
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

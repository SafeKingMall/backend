package com.safeking.shop.domain.payment.web.controller;

import com.safeking.shop.domain.order.web.query.service.ValidationOrderService;
import com.safeking.shop.domain.payment.domain.entity.SafekingPayment;
import com.safeking.shop.domain.payment.web.client.dto.request.PaymentCallbackRequest;
import com.safeking.shop.domain.payment.web.client.dto.request.PaymentCancelRequest;
import com.safeking.shop.domain.payment.web.client.dto.request.PaymentWebhookRequest;
import com.safeking.shop.domain.payment.web.client.dto.response.PaymentResponse;
import com.safeking.shop.domain.payment.web.client.dto.response.PaymentCallbackResponse;
import com.safeking.shop.domain.payment.web.client.service.IamportService;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.safeking.shop.global.jwt.TokenUtils.AUTH_HEADER;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class PaymentController {
    private final IamportService iamportService;
    private final ValidationOrderService validationOrderService;

    /**
     * 콜백으로 결제 구현
     */
    @PostMapping("/payment")
    public ResponseEntity<PaymentResponse> payment(@Valid @RequestBody PaymentCallbackRequest paymentCallbackRequest, HttpServletRequest request) {

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
    public ResponseEntity<IamportResponse<Payment>> cancelPayment(@Valid @RequestBody PaymentCancelRequest paymentCancelRequest, HttpServletRequest request) {

        //validationOrderService.validationMember(request.getHeader(AUTH_HEADER));

        // 결제 취소
        IamportResponse<Payment> response = iamportService.cancel(
                paymentCancelRequest.getImpUid(),
                paymentCancelRequest.getMerchantUid(),
                paymentCancelRequest.getReason(),
                iamportService.getSafekingPayment(paymentCancelRequest.getMerchantUid())
        );

        return new ResponseEntity<>(response, OK);
    }
}

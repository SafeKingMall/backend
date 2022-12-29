package com.safeking.shop.domain.payment.web.controller;

import com.safeking.shop.domain.payment.web.client.dto.request.PaymentRequest;
import com.safeking.shop.domain.payment.web.client.dto.request.PaymentWebhookRequest;
import com.safeking.shop.domain.payment.web.client.dto.response.PaymentResponse;
import com.safeking.shop.domain.payment.web.client.dto.response.PaymentCallbackResponse;
import com.safeking.shop.domain.payment.web.client.service.IamportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class PaymentController {
    private final IamportService iamportService;


    /**
     * 콜백으로 결제 구현
     */
    @PostMapping("/payment")
    public ResponseEntity<PaymentResponse> payment(@Valid @RequestBody PaymentRequest request) {
        /**
         * 1. 클라이언트 결제 요청
         * 2. 아임포트 결제 내역 조회
         * 3. 아임포트 결제 내역, Safeking 결제 내역 비교
         */
        PaymentResponse<PaymentCallbackResponse> response = iamportService.paymentByCallback(request);

        return new ResponseEntity<>(response, OK);
    }

    /**
     * 아임포트 웹훅 사용
     */
    @PostMapping("/payment/webhook")
    public ResponseEntity<?> paymentWebhook(@Valid @RequestBody PaymentWebhookRequest request) {

        iamportService.paymentByWebhook(request);

        return new ResponseEntity<>(OK);
    }
}

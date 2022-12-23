package com.safeking.shop.domain.payment.web.controller;

import com.safeking.shop.domain.payment.web.client.service.IamportService;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class PaymentController {
    private final IamportService iamportService;

    @PostMapping("/payment")
    public String payment() {
        /**
         * 1. 클라이언트 결제 요청
         * 2. 아임포트 결제 내역 조회
         * 3. 아임포트 결제 내역, Safeking 결제 내역 비교
         */
        return "";
    }
}

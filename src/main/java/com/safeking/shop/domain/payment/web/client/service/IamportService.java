package com.safeking.shop.domain.payment.web.client.service;

import com.safeking.shop.domain.payment.web.client.dto.request.PaymentRequest;
import com.safeking.shop.domain.payment.web.client.dto.response.PaymentResponse;
import com.safeking.shop.domain.payment.web.client.dto.response.PaymentSuccessResponse;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

import java.util.Optional;

public interface IamportService {
    //결제내역 단건 조회
    IamportResponse<Payment> findPaymentByImpUid(String impUid);
    // 결제 취소
    // 결제내역 복수 조회
    // 결제상태 기준 복수 조회

    // 결제
    PaymentResponse<PaymentSuccessResponse> payment(PaymentRequest paymentRequest);

}

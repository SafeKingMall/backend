package com.safeking.shop.domain.payment.web.client.service;

import com.safeking.shop.domain.payment.domain.entity.SafekingPayment;
import com.safeking.shop.domain.payment.web.client.dto.request.PaymentAuthCancelRequest;
import com.safeking.shop.domain.payment.web.client.dto.request.PaymentCallbackRequest;
import com.safeking.shop.domain.payment.web.client.dto.request.PaymentWebhookRequest;
import com.safeking.shop.domain.payment.web.client.dto.response.PaymentCancelResponse;
import com.safeking.shop.domain.payment.web.client.dto.response.PaymentResponse;
import com.safeking.shop.domain.payment.web.client.dto.response.PaymentCallbackResponse;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

public interface IamportService {
    // 결제 인증 취소
    String authCancel(PaymentAuthCancelRequest request);
    // 결제
    PaymentResponse<PaymentCallbackResponse> paymentByCallback(PaymentCallbackRequest request);
    void paymentByWebhook(PaymentWebhookRequest request);
    PaymentCancelResponse cancel(String impUid, String merchantUid, String cancelReason, Double returnFee, SafekingPayment findSafekingPayment);
    SafekingPayment getSafekingPayment(String merchantUid);
}

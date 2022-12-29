package com.safeking.shop.domain.payment.web.client.service;

import com.safeking.shop.domain.payment.web.client.dto.request.PaymentRequest;
import com.safeking.shop.domain.payment.web.client.dto.request.PaymentWebhookRequest;
import com.safeking.shop.domain.payment.web.client.dto.response.PaymentResponse;
import com.safeking.shop.domain.payment.web.client.dto.response.PaymentCallbackResponse;

public interface IamportService {
    // 결제
    PaymentResponse<PaymentCallbackResponse> paymentByCallback(PaymentRequest request);
    void paymentByWebhook(PaymentWebhookRequest request);

}

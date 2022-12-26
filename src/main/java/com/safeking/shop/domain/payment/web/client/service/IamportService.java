package com.safeking.shop.domain.payment.web.client.service;

import com.safeking.shop.domain.payment.web.client.dto.request.PaymentRequest;
import com.safeking.shop.domain.payment.web.client.dto.request.PaymentWebhookRequest;
import com.safeking.shop.domain.payment.web.client.dto.response.PaymentResponse;
import com.safeking.shop.domain.payment.web.client.dto.response.PaymentSuccessResponse;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

import java.util.Optional;

public interface IamportService {
    // 결제
    PaymentResponse<PaymentSuccessResponse> paymentByCallback(PaymentRequest request);
    PaymentResponse<PaymentSuccessResponse> paymentByWebhook(PaymentWebhookRequest request);

}

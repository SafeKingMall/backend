package com.safeking.shop.domain.payment.web.client.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentCancelResponse {
    private String message;
    private PaymentCancelPaymentResponse payment;

    @Builder
    public PaymentCancelResponse(String message, PaymentCancelPaymentResponse payment) {
        this.message = message;
        this.payment = payment;
    }
}

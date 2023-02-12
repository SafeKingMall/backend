package com.safeking.shop.domain.payment.web.client.dto.response.askcancel;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentAskCancelResponse {
    private String message;
    private PaymentAskCancelOrderResponse order;
    private PaymentAskCancelPaymentResponse payment;
    private PaymentAskCancelDeliveryResponse delivery;

    @Builder
    public PaymentAskCancelResponse(String message, PaymentAskCancelOrderResponse order, PaymentAskCancelPaymentResponse payment, PaymentAskCancelDeliveryResponse delivery) {
        this.message = message;
        this.order = order;
        this.payment = payment;
        this.delivery = delivery;
    }
}

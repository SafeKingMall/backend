package com.safeking.shop.domain.payment.web.client.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentCancelPaymentResponse {
    private Double amount; // 환불 금액
    private String buyerName; // 주문자명

    @Builder
    public PaymentCancelPaymentResponse(Double amount, String buyerName) {
        this.amount = amount;
        this.buyerName = buyerName;
    }
}

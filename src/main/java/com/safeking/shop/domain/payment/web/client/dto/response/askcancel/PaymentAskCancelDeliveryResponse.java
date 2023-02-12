package com.safeking.shop.domain.payment.web.client.dto.response.askcancel;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentAskCancelDeliveryResponse {
    private String status; // 배송 상태

    @Builder
    public PaymentAskCancelDeliveryResponse(String status, Integer cost) {
        this.status = status;
    }
}

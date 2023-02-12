package com.safeking.shop.domain.payment.web.client.dto.response.canceldetail;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentCancelDetailResponse {
    private String message;
    private PaymentCancelDetailOrderResponse order;

    @Builder
    public PaymentCancelDetailResponse(String message, PaymentCancelDetailOrderResponse order) {
        this.message = message;
        this.order = order;
    }
}

package com.safeking.shop.domain.payment.web.client.dto.response.cancellist;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentCancelListOrderItemsResponse {
    private Long id;
    private String name;

    @Builder
    public PaymentCancelListOrderItemsResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}

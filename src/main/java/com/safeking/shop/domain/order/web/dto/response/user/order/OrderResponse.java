package com.safeking.shop.domain.order.web.dto.response.user.order;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderResponse {
    private String message;
    private OrderOrderResponse order;

    @Builder
    public OrderResponse(String message, OrderOrderResponse order) {
        this.message = message;
        this.order = order;
    }
}

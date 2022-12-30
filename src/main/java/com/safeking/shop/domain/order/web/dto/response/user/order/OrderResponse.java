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
    private OrderDeliveryResponse delivery;

    @Builder
    public OrderResponse(String message, OrderOrderResponse order, OrderDeliveryResponse delivery) {
        this.message = message;
        this.order = order;
        this.delivery = delivery;
    }
}

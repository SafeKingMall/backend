package com.safeking.shop.domain.order.web.dto.response.user.orderinfo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderInfoResponse {
    private String message;
    private OrderInfoOrderResponse order;
    private OrderInfoDeliveryResponse delivery;

    @Builder
    public OrderInfoResponse(String message, OrderInfoOrderResponse order, OrderInfoDeliveryResponse delivery) {
        this.message = message;
        this.order = order;
        this.delivery = delivery;
    }
}

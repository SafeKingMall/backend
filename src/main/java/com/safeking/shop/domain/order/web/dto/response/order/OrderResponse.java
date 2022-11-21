package com.safeking.shop.domain.order.web.dto.response.order;

import lombok.Builder;
import lombok.Data;

@Data
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

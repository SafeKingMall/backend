package com.safeking.shop.domain.order.web.dto.response.findorder;

import lombok.Builder;
import lombok.Data;

@Data
public class FindOrderResponse {
    private String message;
    private OrderResponse order;
    private OrderDeliveryResponse delivery;

    @Builder
    public FindOrderResponse(String message, OrderResponse order, OrderDeliveryResponse delivery) {
        this.message = message;
        this.order = order;
        this.delivery = delivery;
    }
}

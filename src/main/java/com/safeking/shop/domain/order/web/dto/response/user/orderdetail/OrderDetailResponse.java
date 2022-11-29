package com.safeking.shop.domain.order.web.dto.response.user.orderdetail;

import lombok.Builder;
import lombok.Data;

@Data
public class OrderDetailResponse {
    private String message;
    private OrderDetailOrderResponse order;

    @Builder
    public OrderDetailResponse(String message, OrderDetailOrderResponse order) {
        this.message = message;
        this.order = order;
    }
}

package com.safeking.shop.domain.order.web.dto.response.user.orderdetail;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderDetailResponse {
    private String message;
    private OrderDetailOrderResponse order;

    @Builder
    public OrderDetailResponse(String message, OrderDetailOrderResponse order) {
        this.message = message;
        this.order = order;
    }
}

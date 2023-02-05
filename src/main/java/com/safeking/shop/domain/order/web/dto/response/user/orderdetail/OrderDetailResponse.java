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
    private OrderDetailMember member;

    @Builder
    public OrderDetailResponse(String message, OrderDetailOrderResponse order, OrderDetailMember member) {
        this.message = message;
        this.order = order;
        this.member = member;
    }
}

package com.safeking.shop.domain.order.web.dto.response.user.search;

import lombok.Builder;
import lombok.Data;

@Data
public class OrderListPaymentResponse {
    private String status;

    @Builder
    public OrderListPaymentResponse(String status) {
        this.status = status;
    }
}

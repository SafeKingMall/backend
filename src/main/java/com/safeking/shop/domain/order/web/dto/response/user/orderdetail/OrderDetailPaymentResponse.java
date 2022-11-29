package com.safeking.shop.domain.order.web.dto.response.user.orderdetail;

import lombok.Builder;
import lombok.Data;

@Data
public class OrderDetailPaymentResponse {
    private String status;

    @Builder
    public OrderDetailPaymentResponse(String status) {
        this.status = status;
    }
}

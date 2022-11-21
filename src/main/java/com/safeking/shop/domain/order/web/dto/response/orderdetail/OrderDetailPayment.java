package com.safeking.shop.domain.order.web.dto.response.orderdetail;

import lombok.Builder;
import lombok.Data;

@Data
public class OrderDetailPayment {
    private String status;

    @Builder
    public OrderDetailPayment(String status) {
        this.status = status;
    }
}

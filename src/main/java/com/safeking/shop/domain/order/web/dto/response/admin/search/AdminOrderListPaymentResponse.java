package com.safeking.shop.domain.order.web.dto.response.admin.search;

import lombok.Builder;
import lombok.Data;

@Data
public class AdminOrderListPaymentResponse {
    private String status;

    @Builder
    public AdminOrderListPaymentResponse(String status) {
        this.status = status;
    }
}

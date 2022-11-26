package com.safeking.shop.domain.order.web.dto.response.admin.orderdetail;

import lombok.Builder;
import lombok.Data;

@Data
public class AdminOrderDetailResponse {
    private String message;
    private AdminOrderDetailOrderResponse order;

    @Builder
    public AdminOrderDetailResponse(String message, AdminOrderDetailOrderResponse order) {
        this.message = message;
        this.order = order;
    }
}

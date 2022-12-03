package com.safeking.shop.domain.order.web.dto.response.admin.search;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class AdminOrderListResponse {
    private String message;
    private List<AdminOrderListOrderResponse> orders;

    @Builder
    public AdminOrderListResponse(String message, List<AdminOrderListOrderResponse> orders) {
        this.message = message;
        this.orders = orders;
    }
}

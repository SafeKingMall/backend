package com.safeking.shop.domain.order.web.dto.response.admin.search;

import lombok.Builder;
import lombok.Data;

@Data
public class AdminOrderListOrderItemResponse {
    private Long id;
    private String name;

    @Builder
    public AdminOrderListOrderItemResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}

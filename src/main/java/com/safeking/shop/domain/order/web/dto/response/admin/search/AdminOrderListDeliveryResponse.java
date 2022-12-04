package com.safeking.shop.domain.order.web.dto.response.admin.search;

import lombok.Builder;
import lombok.Data;

@Data
public class AdminOrderListDeliveryResponse {
    private String receiver;
    private String status;

    @Builder
    public AdminOrderListDeliveryResponse(String receiver, String status) {
        this.receiver = receiver;
        this.status = status;
    }
}

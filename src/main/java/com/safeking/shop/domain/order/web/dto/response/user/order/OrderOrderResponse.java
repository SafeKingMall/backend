package com.safeking.shop.domain.order.web.dto.response.user.order;

import lombok.Builder;
import lombok.Data;

@Data
public class OrderOrderResponse {
    private Long id;
    private String memo;

    @Builder
    public OrderOrderResponse(Long id, String memo) {
        this.id = id;
        this.memo = memo;
    }
}

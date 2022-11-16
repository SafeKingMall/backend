package com.safeking.shop.domain.order.web.dto.response.findorder;

import lombok.Builder;
import lombok.Data;

@Data
public class OrderResponse {
    private Long id;
    private String memo;

    @Builder
    public OrderResponse(Long id, String memo) {
        this.id = id;
        this.memo = memo;
    }
}

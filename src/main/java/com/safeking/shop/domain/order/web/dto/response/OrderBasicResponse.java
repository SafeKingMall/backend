package com.safeking.shop.domain.order.web.dto.response;

import lombok.Data;

@Data
public class OrderBasicResponse {
    String message;

    public OrderBasicResponse(String message) {
        this.message = message;
    }
}

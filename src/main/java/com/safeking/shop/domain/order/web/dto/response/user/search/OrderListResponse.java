package com.safeking.shop.domain.order.web.dto.response.user.search;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderListResponse {
    private String message;
    private List<OrderListOrdersResponse> orders;

    @Builder
    public OrderListResponse(String message, List<OrderListOrdersResponse> orders) {
        this.message = message;
        this.orders = orders;
    }
}

package com.safeking.shop.domain.order.web.dto.response.admin.search;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AdminOrderListResponse {
    private String message;
    private List<AdminOrderListOrderResponse> orders;

    @Builder
    public AdminOrderListResponse(String message, List<AdminOrderListOrderResponse> orders) {
        this.message = message;
        this.orders = orders;
    }
}

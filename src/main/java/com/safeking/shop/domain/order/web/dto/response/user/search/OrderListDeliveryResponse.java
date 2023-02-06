package com.safeking.shop.domain.order.web.dto.response.user.search;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderListDeliveryResponse {
    private String status;

    @Builder
    public OrderListDeliveryResponse(String status) {
        this.status = status;
    }
}

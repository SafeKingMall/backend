package com.safeking.shop.domain.order.web.dto.response.admin.search;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AdminOrderListDeliveryResponse {
    private String receiver;
    private String status;

    @Builder
    public AdminOrderListDeliveryResponse(String receiver, String status) {
        this.receiver = receiver;
        this.status = status;
    }
}

package com.safeking.shop.domain.order.web.dto.response.admin.orderdetail;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AdminOrderDetailResponse {
    private String message;
    private AdminOrderDetailOrderResponse order;

    @Builder
    public AdminOrderDetailResponse(String message, AdminOrderDetailOrderResponse order) {
        this.message = message;
        this.order = order;
    }
}

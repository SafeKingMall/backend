package com.safeking.shop.domain.order.web.dto.response.admin.search;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AdminOrderListOrderItemResponse {
    private Long id;
    private String name;

    @Builder
    public AdminOrderListOrderItemResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}

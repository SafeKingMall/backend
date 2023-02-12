package com.safeking.shop.domain.order.web.dto.response.user.search;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderListOrderItemResponse {
    private Long id;
    private String name;
    private String thumbnail;

    @Builder
    public OrderListOrderItemResponse(Long id, String name, String thumbnail) {
        this.id = id;
        this.name = name;
        this.thumbnail = thumbnail;
    }
}

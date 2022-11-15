package com.safeking.shop.domain.order.web.dto.request.order;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderItemDto {
    private Long id;
    private int count;

    public OrderItemDto(Long id, int count) {
        this.id = id;
        this.count = count;
    }
}

package com.safeking.shop.domain.order.web.query.repository.querydto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AdminOrderListOrderItemQueryDto {
    private Long id;
    private String name;

    @QueryProjection
    public AdminOrderListOrderItemQueryDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}

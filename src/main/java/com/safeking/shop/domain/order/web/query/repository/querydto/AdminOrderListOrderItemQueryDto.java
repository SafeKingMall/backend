package com.safeking.shop.domain.order.web.query.repository.querydto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AdminOrderListOrderItemQueryDto {

    @JsonIgnore
    private Long orderId;
    private Long id;
    private String name;

    @QueryProjection
    public AdminOrderListOrderItemQueryDto(Long orderId, Long id, String name) {
        this.orderId = orderId;
        this.id = id;
        this.name = name;
    }
}

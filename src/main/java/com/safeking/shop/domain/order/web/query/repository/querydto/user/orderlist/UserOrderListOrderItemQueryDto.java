package com.safeking.shop.domain.order.web.query.repository.querydto.user.orderlist;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserOrderListOrderItemQueryDto {
    @JsonIgnore
    private Long orderId;
    private Long id;
    private String name;
    private String thumbnail;

    @QueryProjection
    public UserOrderListOrderItemQueryDto(Long orderId, Long id, String name, String thumbnail) {
        this.orderId = orderId;
        this.id = id;
        this.name = name;
        this.thumbnail = thumbnail;
    }
}

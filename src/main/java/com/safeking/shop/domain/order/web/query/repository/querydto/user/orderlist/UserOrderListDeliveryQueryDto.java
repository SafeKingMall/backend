package com.safeking.shop.domain.order.web.query.repository.querydto.user.orderlist;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserOrderListDeliveryQueryDto {
    private String status;

    @QueryProjection
    public UserOrderListDeliveryQueryDto(String status) {
        this.status = status;
    }
}

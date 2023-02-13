package com.safeking.shop.domain.order.web.query.repository.querydto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AdminOrderListMemberQueryDto {
    private String name;

    @QueryProjection
    public AdminOrderListMemberQueryDto(String name) {
        this.name = name;
    }
}

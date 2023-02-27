package com.safeking.shop.domain.order.web.query.repository.querydto;

import com.querydsl.core.annotations.QueryProjection;
import com.safeking.shop.domain.order.domain.entity.status.OrderStatus;
import lombok.Data;

@Data
public class StatusAndImpUid {
    private String status;
    private String impUid;

    @QueryProjection
    public StatusAndImpUid(OrderStatus status, String impUid) {
        this.status = status.getDescription();
        this.impUid = impUid;
    }
}

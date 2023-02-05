package com.safeking.shop.domain.order.web.dto.response.user.orderinfo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderInfoOrderResponse {
    private Long id;
    private String memo;
    private String merchantUid;

    @Builder
    public OrderInfoOrderResponse(Long id, String memo, String merchantUid) {
        this.id = id;
        this.memo = memo;
        this.merchantUid = merchantUid;
    }
}

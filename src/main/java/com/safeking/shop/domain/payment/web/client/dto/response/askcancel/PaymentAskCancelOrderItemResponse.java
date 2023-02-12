package com.safeking.shop.domain.payment.web.client.dto.response.askcancel;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentAskCancelOrderItemResponse {
    private String name; // 상품명
    private Integer price; // 판매가
    private Integer count; // 수량
    private String thumbnail;

    @Builder
    public PaymentAskCancelOrderItemResponse(String name, Integer price, Integer count, String thumbnail) {
        this.name = name;
        this.price = price;
        this.count = count;
        this.thumbnail = thumbnail;
    }
}

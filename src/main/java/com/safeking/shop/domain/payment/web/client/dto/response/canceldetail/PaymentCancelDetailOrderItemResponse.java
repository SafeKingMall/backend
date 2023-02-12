package com.safeking.shop.domain.payment.web.client.dto.response.canceldetail;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentCancelDetailOrderItemResponse {
    private String name; // 상품명
    private Integer price; // 상품 가격
    private Integer count; // 수량
    private Long orderPrice; // 주문 금액 = 상품가격 * 수량
    private String thumbnail; // 섬네일

    @Builder
    public PaymentCancelDetailOrderItemResponse(String name, Integer price, Integer count, String thumbnail) {
        this.name = name;
        this.price = price;
        this.count = count;
        this.orderPrice = price.longValue() * count.longValue();
        this.thumbnail = thumbnail;
    }
}

package com.safeking.shop.domain.order.web.dto.response.user.orderdetail;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderDetailOrderItemResponse {
    private String name; // 상품명
    private Integer count; // 주문 수량
    private Integer price; // 판매가
    private Long orderPrice; // 주문금액 = 판매가 * 주문수량
    private String thumbnail; // 썸네일

    @Builder
    public OrderDetailOrderItemResponse(String name, Integer count, Integer price, String thumbnail) {
        this.name = name;
        this.count = count;
        this.price = price;
        this.thumbnail = thumbnail;
        this.orderPrice = price.longValue() * count.longValue();
    }
}

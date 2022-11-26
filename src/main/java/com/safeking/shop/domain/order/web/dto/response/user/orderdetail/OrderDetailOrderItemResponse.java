package com.safeking.shop.domain.order.web.dto.response.user.orderdetail;

import lombok.Builder;
import lombok.Data;

@Data
public class OrderDetailOrderItemResponse {
    private Long id;
    private String name;
    private int count;
    private int price;

    @Builder
    public OrderDetailOrderItemResponse(Long id, String name, int count, int price) {
        this.id = id;
        this.name = name;
        this.count = count;
        this.price = price;
    }
}
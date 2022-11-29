package com.safeking.shop.domain.order.web.dto.response.user.search;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderListOrdersResponse {
    private Long id;
    private String status;
    private int price;
    private LocalDateTime date;
    private int count;
    private OrderListOrderItemResponse orderItem;
    private OrderListPaymentResponse payment;

    @Builder
    public OrderListOrdersResponse(Long id, String status, int price, LocalDateTime date, int count, OrderListOrderItemResponse orderItem, OrderListPaymentResponse payment) {
        this.id = id;
        this.status = status;
        this.price = price;
        this.date = date;
        this.count = count;
        this.orderItem = orderItem;
        this.payment = payment;
    }
}

package com.safeking.shop.domain.order.web.dto.response.orderdetail;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class OrderDetailOrderResponse {
    private Long id;
    private String status;
    private int price;
    private String memo;
    private String date;
    private List<OrderDetailItem> orderItems;
    private OrderDetailPayment payment;
    private OrderDetailDelivery delivery;

    @Builder
    public OrderDetailOrderResponse(Long id, String status, int price, String memo, String date, List<OrderDetailItem> orderItems, OrderDetailPayment payment, OrderDetailDelivery delivery) {
        this.id = id;
        this.status = status;
        this.price = price;
        this.memo = memo;
        this.date = date;
        this.orderItems = orderItems;
        this.payment = payment;
        this.delivery = delivery;
    }
}

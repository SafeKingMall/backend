package com.safeking.shop.domain.order.web.dto.response.user.orderdetail;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
public class OrderDetailOrderResponse {
    private Long id;
    private String status;
    private int price;
    private String memo;
    private String date;
    private List<OrderDetailOrderItemResponse> orderItems;
    private OrderDetailPaymentResponse payment;
    private OrderDetailDeliveryResponse delivery;

    @Builder
    public OrderDetailOrderResponse(Long id, String status, int price, String memo, LocalDateTime date, List<OrderDetailOrderItemResponse> orderItems, OrderDetailPaymentResponse payment, OrderDetailDeliveryResponse delivery) {
        this.id = id;
        this.status = status;
        this.price = price;
        this.memo = memo;
        this.date = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));;
        this.orderItems = orderItems;
        this.payment = payment;
        this.delivery = delivery;
    }
}

package com.safeking.shop.domain.order.web.dto.response.admin.orderdetail;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AdminOrderDetailOrderResponse {
    private Long id;
    private String status;
    private int price;
    private String date;
    private String adminMemo;
    private List<AdminOrderDetailOrderItemsResponse> orderItems;
    private AdminOrderDetailDeliveryResponse delivery;
    private AdminOrderDetailPaymentResponse payment;

    @Builder
    public AdminOrderDetailOrderResponse(Long id, String status, int price, String date, String adminMemo, List<AdminOrderDetailOrderItemsResponse> orderItems, AdminOrderDetailDeliveryResponse delivery, AdminOrderDetailPaymentResponse payment) {
        this.id = id;
        this.status = status;
        this.price = price;
        this.date = date;
        this.adminMemo = adminMemo;
        this.orderItems = orderItems;
        this.delivery = delivery;
        this.payment = payment;
    }
}

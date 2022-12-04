package com.safeking.shop.domain.order.web.dto.response.admin.orderdetail;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AdminOrderDetailOrderResponse {
    private Long id;
    private String status;
    private int price;
    private String date;
    private String memo;
    private String adminMemo;
    private List<AdminOrderDetailOrderItemsResponse> orderItems;
    private AdminOrderDetailDeliveryResponse delivery;
    private AdminOrderDetailPaymentResponse payment;

    @Builder
    public AdminOrderDetailOrderResponse(Long id, String status, int price, String memo, LocalDateTime date, String adminMemo, List<AdminOrderDetailOrderItemsResponse> orderItems, AdminOrderDetailDeliveryResponse delivery, AdminOrderDetailPaymentResponse payment) {
        this.id = id;
        this.status = status;
        this.price = price;
        this.memo = memo;
        this.date = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        this.adminMemo = adminMemo;
        this.orderItems = orderItems;
        this.delivery = delivery;
        this.payment = payment;
    }
}

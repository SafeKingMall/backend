package com.safeking.shop.domain.order.web.dto.response.admin.search;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AdminOrderListOrderResponse {
    private Long id;
    private String status;
    private int price;
    private LocalDateTime date;
    private int orderItemCount;
    private AdminOrderListOrderItemResponse orderItem;
    private AdminOrderListPaymentResponse payment;
    private AdminOrderListMemberResponse member;
    private AdminOrderListDeliveryResponse delivery;

    @Builder
    public AdminOrderListOrderResponse(Long id, String status, int price, LocalDateTime date, int orderItemCount, AdminOrderListOrderItemResponse orderItem, AdminOrderListPaymentResponse payment, AdminOrderListMemberResponse member, AdminOrderListDeliveryResponse delivery) {
        this.id = id;
        this.status = status;
        this.price = price;
        this.date = date;
        this.orderItemCount = orderItemCount;
        this.orderItem = orderItem;
        this.payment = payment;
        this.member = member;
        this.delivery = delivery;
    }
}

package com.safeking.shop.domain.order.web.dto.response.user.orderdetail;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderDetailOrderResponse {
    private Long id;
    private String status;
    private int price;
    private String memo;
    private String date;
    private List<OrderDetailOrderItemResponse> orderItems;
    private OrderDetailPaymentResponse payment;
    private OrderDetailDeliveryResponse delivery;
    private String merchantUid;
    private String cancelReason;

    @Builder
    public OrderDetailOrderResponse(Long id, String status, int price, String memo, LocalDateTime date, List<OrderDetailOrderItemResponse> orderItems, OrderDetailPaymentResponse payment, OrderDetailDeliveryResponse delivery, String merchantUid, String cancelReason) {
        this.id = id;
        this.status = status;
        this.price = price;
        this.memo = memo;
        this.date = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));;
        this.orderItems = orderItems;
        this.payment = payment;
        this.delivery = delivery;
        this.merchantUid = merchantUid;
        this.cancelReason = cancelReason;
    }
}

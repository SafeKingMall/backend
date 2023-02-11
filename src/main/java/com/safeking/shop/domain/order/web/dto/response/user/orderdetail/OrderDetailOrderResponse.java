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
    private String memo; // 주문 요청사항
    private String date; // 주문일시
    private String merchantUid; // 주문 번호
    private List<OrderDetailOrderItemResponse> orderItems;
    private OrderDetailPaymentResponse payment;
    private OrderDetailDeliveryResponse delivery;

    @Builder
    public OrderDetailOrderResponse(String memo, LocalDateTime date, List<OrderDetailOrderItemResponse> orderItems, OrderDetailPaymentResponse payment, OrderDetailDeliveryResponse delivery, String merchantUid) {
        this.memo = memo;
        this.date = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));;
        this.orderItems = orderItems;
        this.payment = payment;
        this.delivery = delivery;
        this.merchantUid = merchantUid;
    }
}

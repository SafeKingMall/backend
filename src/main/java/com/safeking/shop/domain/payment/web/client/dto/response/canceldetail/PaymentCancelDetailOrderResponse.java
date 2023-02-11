package com.safeking.shop.domain.payment.web.client.dto.response.canceldetail;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentCancelDetailOrderResponse {
    private String merchantUid; // 주문 번호
    private String date; // 주문 일시
    private List<PaymentCancelDetailOrderItemResponse> orderItems;
    private PaymentCancelDetailPaymentResponse payment;
    private PaymentCancelDetailDeliveryResponse delivery;

    @Builder
    public PaymentCancelDetailOrderResponse(String merchantUid, LocalDateTime date, List<PaymentCancelDetailOrderItemResponse> orderItems, PaymentCancelDetailPaymentResponse payment, PaymentCancelDetailDeliveryResponse delivery) {
        this.merchantUid = merchantUid;
        this.date = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        this.orderItems = orderItems;
        this.payment = payment;
        this.delivery = delivery;
    }
}

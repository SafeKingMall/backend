package com.safeking.shop.domain.payment.web.client.dto.response.cancellist;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentCancelListOrderResponse {
    private Long id;
    private String status;
    private String date;
    private Integer count;
    private String merchantUid;
    private PaymentCancelListOrderItemsResponse orderItem;
    private PaymentCancelListPaymentResponse payment;

    @Builder
    public PaymentCancelListOrderResponse(Long id, String status, LocalDateTime date, Integer count, String merchantUid, PaymentCancelListOrderItemsResponse orderItem, PaymentCancelListPaymentResponse payment) {
        this.id = id;
        this.status = status;
        this.date = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        this.count = count;
        this.merchantUid = merchantUid;
        this.orderItem = orderItem;
        this.payment = payment;
    }
}

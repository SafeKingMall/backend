package com.safeking.shop.domain.payment.web.client.dto.response.askcancel;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentAskCancelOrderResponse {
    private Long id;
    private String merchantUid; // 주문 번호
    private String date; // 주문 일시
    private List<PaymentAskCancelOrderItemResponse> orderItem;

    @Builder
    public PaymentAskCancelOrderResponse(Long id, String merchantUid, LocalDateTime date, List<PaymentAskCancelOrderItemResponse> orderItem) {
        this.id = id;
        this.merchantUid = merchantUid;
        this.date = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        this.orderItem = orderItem;
    }
}

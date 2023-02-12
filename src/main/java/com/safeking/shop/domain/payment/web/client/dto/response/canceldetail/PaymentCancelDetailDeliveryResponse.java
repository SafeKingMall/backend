package com.safeking.shop.domain.payment.web.client.dto.response.canceldetail;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentCancelDetailDeliveryResponse {
    private String status; // 배송 상태
    private Integer cost; // 배송료
    private String invoiceNumber; // 송장 번호
    private String company; // 택배사

    @Builder
    public PaymentCancelDetailDeliveryResponse(String status, Integer cost, String invoiceNumber, String company) {
        this.status = status;
        this.cost = cost;
        this.invoiceNumber = invoiceNumber;
        this.company = company;
    }
}

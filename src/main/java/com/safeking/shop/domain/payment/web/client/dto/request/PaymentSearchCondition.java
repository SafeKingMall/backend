package com.safeking.shop.domain.payment.web.client.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
public class PaymentSearchCondition {
    private String fromDate;
    private String toDate;
    private String paymentStatus;

    @Builder
    public PaymentSearchCondition(String fromDate, String toDate, String paymentStatus) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.paymentStatus = paymentStatus;
    }
}

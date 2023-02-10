package com.safeking.shop.domain.payment.web.client.dto.response.cancellist;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentCancelListPaymentResponse {
    private String status;
    private String paidDate;
    private String canceledDate;
    private Integer price;

    @Builder
    public PaymentCancelListPaymentResponse(String status, Date paidDate, LocalDateTime canceledDate, Integer price) {
        this.status = status;
        this.price = price;
        if(paidDate != null) {
            this.paidDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(paidDate);
        }
        if(canceledDate != null) {
            this.canceledDate = canceledDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        }
    }
}

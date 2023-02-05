package com.safeking.shop.domain.order.web.dto.response.user.search;

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
public class OrderListPaymentResponse {
    private String status;
    private String paidDate; // 결제 완료 일시
    private String canceledDate; // 결제 취소 일시

    @Builder
    public OrderListPaymentResponse(String status, Date paidDate, LocalDateTime canceledDate) {
        this.status = status;
        this.paidDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(paidDate);
        this.canceledDate = canceledDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
    }
}

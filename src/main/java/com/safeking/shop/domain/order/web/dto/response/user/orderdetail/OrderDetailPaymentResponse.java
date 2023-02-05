package com.safeking.shop.domain.order.web.dto.response.user.orderdetail;

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
public class OrderDetailPaymentResponse {
    private String status;
    private String impUid;
    private String cancelReason;
    private String canceledRequestDate; // 결제 취소 접수 일시
    private String canceledDate; // 결제 취소 일시
    private String paidDate;
    private String failedDate;

    @Builder
    public OrderDetailPaymentResponse(String status, String impUid, String cancelReason, LocalDateTime canceledRequestDate, LocalDateTime canceledDate, Date paidDate, LocalDateTime failedDate) {
        this.status = status;
        this.impUid = impUid;
        this.cancelReason = cancelReason;
        this.canceledRequestDate = canceledRequestDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        this.canceledDate = canceledDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        this.failedDate = failedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        this.paidDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(paidDate);
    }
}

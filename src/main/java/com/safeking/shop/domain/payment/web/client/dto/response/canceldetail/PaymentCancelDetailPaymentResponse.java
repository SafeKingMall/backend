package com.safeking.shop.domain.payment.web.client.dto.response.canceldetail;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentCancelDetailPaymentResponse {

    private String status; // 결제 상태
    private String buyerName; // 취소 요청자
    private String canceledRequestDate; // 취소 접수일자
    private String canceledDate; // 취소 완료일
    private String buyerTel; // 연락처
    private String buyerAddr; // 취소 요청자 주소
    private String cancelReason; // 취소 사유
    private Integer cancelAmount; // 환불완료 금액
    private String payMethod; // 환불 수단
    private String cardCompany; // 카드사
    private Integer refundFee; // 반품비 = 결제금액 - 환불완료 금액

    @Builder
    public PaymentCancelDetailPaymentResponse(String status, String buyerName, LocalDateTime canceledRequestDate, LocalDateTime canceledDate,
                                              String buyerTel, String buyerAddr, String cancelReason, Integer cancelAmount, String payMethod,
                                              String cardCompany, Integer refundFee) {
        this.status = status;
        this.buyerName = buyerName;
        this.buyerTel = buyerTel;
        this.buyerAddr = buyerAddr;
        this.cancelReason = cancelReason;
        this.cancelAmount = cancelAmount;
        this.payMethod = payMethod;
        this.cardCompany = cardCompany;
        this.refundFee = refundFee;
        if(canceledRequestDate != null) this.canceledRequestDate = canceledRequestDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        if(canceledDate != null) this.canceledDate = canceledDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
    }
}

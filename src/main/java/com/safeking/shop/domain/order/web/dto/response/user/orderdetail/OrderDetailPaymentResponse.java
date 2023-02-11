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
    private String status; // 결제 상태
    private String cardCompany; // 카드사
    private String payMethod; // 결제 방식
    private String buyerName; // 입금자명
    private String buyerTel; // 입금자명 연락처
    private String buyerAddr; // 입금자명 주소
    private Integer amount; // 결제금액
    private String cashReceiptMethod; // 현금영수증 방식
    private String businessLicenseNumber;  // 사업자번호

    @Builder
    public OrderDetailPaymentResponse(String status, String cardCompany, String payMethod, String buyerName, String buyerTel, String buyerAddr, Integer amount, String cashReceiptMethod, String businessLicenseNumber) {
        this.status = status;
        this.cardCompany = cardCompany;
        this.payMethod = payMethod;
        this.buyerName = buyerName;
        this.buyerTel = buyerTel;
        this.buyerAddr = buyerAddr;
        this.amount = amount;
        this.cashReceiptMethod = cashReceiptMethod;
        this.businessLicenseNumber = businessLicenseNumber;
    }
}

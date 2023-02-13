package com.safeking.shop.domain.payment.web.client.dto.response.askcancel;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentAskCancelPaymentResponse {
    private String impUid; // 결제 고유 번호
    private String status; // 결제 상태
    private String payMethod; // 결제 방식
    private Integer price; // 결제 금액
    private String buyerName; // 입금자 명
    private String cardCompany; // 카드사
    private String cashReceiptMethod; // 현금영수증 방식
    private String businessLicenseNumber; // 사업자 번호

    @Builder
    public PaymentAskCancelPaymentResponse(String impUid, String status, String payMethod, Integer price, String buyerName, String cardCompany, String cashReceiptMethod, String businessLicenseNumber) {
        this.impUid = impUid;
        this.status = status;
        this.payMethod = payMethod;
        this.price = price;
        this.buyerName = buyerName;
        this.cardCompany = cardCompany;
        this.cashReceiptMethod = cashReceiptMethod;
        this.businessLicenseNumber = businessLicenseNumber;
    }
}

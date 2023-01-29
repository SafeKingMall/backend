package com.safeking.shop.domain.payment.web.client.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentCallbackResponse {
    private String payMethod; //결제 수단
    private String merchantUid; //주문 번호
    private String name; // 상품명
    private Integer amount; // 가격
    private String buyerEmail; // 구매자 이메일
    private String buyerName; // 구매자 이름
    private String buyerTel; // 구매자 연락처
    private String buyerAddr; // 구매자 주소
    private String buyerPostcode; // 구매자 우편번호
    private String errorMsg; // 결제 실패 메시지

    public PaymentCallbackResponse() {
    }

    @Builder
    public PaymentCallbackResponse(String payMethod, String merchantUid, String name, Integer amount, String buyerEmail, String buyerName, String buyerTel, String buyerAddr, String buyerPostcode, String errorMsg) {
        this.payMethod = payMethod;
        this.merchantUid = merchantUid;
        this.name = name;
        this.amount = amount;
        this.buyerEmail = buyerEmail;
        this.buyerName = buyerName;
        this.buyerTel = buyerTel;
        this.buyerAddr = buyerAddr;
        this.buyerPostcode = buyerPostcode;
        this.errorMsg = errorMsg;
    }
}

package com.safeking.shop.domain.payment.web.client.dto.response;

public class PaymentSuccessResponse {
    private String payMethod; //결제 수단
    private String merchantUid; //주문 번호
    private String name; // 상품명
    private String amount; // 가격
    private String buyerEmail; // 구매자 이메일
    private String buyerName; // 구매자 이름
    private String buyerTel; // 구매자 연락처
    private String buyerAddr; // 구매자 주소
    private String buyerPostcode; // 구매자 우편번호
}

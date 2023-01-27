package com.safeking.shop.domain.payment.constant;

public class SafeKingPaymentConst {
    public static final String PAYMENT_PAID_SUCCESS = "결제 완료";
    public static final String PAYMENT_PAID_FAILED = "결제 실패";
    public static final String SAFEKING_PAYMENT_NONE = "DB에 결제 내역이 없습니다. 주문 후에 결제 해주세요.";
    public static final String PAYMENT_AMOUNT_ERROR_WEBHOOK = "결제 금액 검증 에러 발생(웹훅)";
    public static final String PAYMENT_AMOUNT_ERROR_CALLBACK = "결제 금액 검증 에러 발생(콜백)";
    public static final String IAMPORT_PAYMENT_ERROR = "아임포트 에러 발생";
}

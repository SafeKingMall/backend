package com.safeking.shop.domain.payment.constant;

public class SafeKingPaymentConst {
    public static final String PAYMENT_PAID_SUCCESS = "결제 완료";
    public static final String PAYMENT_PAID_CANCEL = "결제 취소";
    public static final String SAFEKING_PAYMENT_NONE = "DB에 결제 내역이 없습니다. 주문 후에 결제 해주세요.";
    public static final String PAYMENT_AMOUNT_DIFFERENT_WEBHOOK = "가맹점 결제금액과 요청 결제금액이 다릅니다.(웹훅)";
    public static final String PAYMENT_AMOUNT_DIFFERENT_CALLBACK = "가맹점 결제금액과 요청 결제금액이 다릅니다.(콜백)";
    public static final String IAMPORT_PAYMENT_ERROR = "아임포트 에러 발생";
    public static final String PAYMENT_REQUEST_CANCEL = "결제 인증 취소요청";
    public static final String PAYMENT_REQUEST_CANCEL_FAIL = "결제 인증 취소요청 실패";
}

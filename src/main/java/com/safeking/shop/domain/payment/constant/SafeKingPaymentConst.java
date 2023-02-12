package com.safeking.shop.domain.payment.constant;

public class SafeKingPaymentConst {
    public static final String PAYMENT_PAID_SUCCESS = "결제 완료";
    public static final String PAYMENT_PAID_FAIL = "결제 성공 실패";
    public static final String PAYMENT_PAID_CANCEL_SUCCESS = "결제 취소 성공";
    public static final String SAFEKING_PAYMENT_NONE = "DB에 결제 내역이 없습니다. 주문 후에 결제 해주세요.";
    public static final String PAYMENT_AMOUNT_DIFFERENT_WEBHOOK = "가맹점 결제금액과 요청 결제금액이 다릅니다.(웹훅)";
    public static final String PAYMENT_AMOUNT_DIFFERENT_CALLBACK = "가맹점 결제금액과 요청 결제금액이 다릅니다.(콜백)";
    public static final String IAMPORT_PAYMENT_ERROR = "아임포트 에러 발생";
    public static final String PAYMENT_REQUEST_CANCEL = "결제 인증 취소요청";
    public static final String PAYMENT_REQUEST_CANCEL_FAIL = "결제 인증 취소요청 실패";
    public static final String REFUND_FEE_CHECK = "반품비가 결제금액 보다 큽니다.";
    public static final String PAYMENT_CANCEL_LIST_FIND_SUCCESS = "환불 내역 조회 성공";
    public static final String PAYMENT_CANCEL_LIST_FIND_FAIL = "환불 내역이 존재하지 않습니다.";
    public static final String PAYMENT_CANCEL_FIND_SUCCESS = "환불신청 단건 조회 성공";
    public static final String PAYMENT_CANCEL_FIND_FAIL = "환불신청 단건 정보가 존재하지 않습니다.";
    public static final String PAYMENT_CANCEL_DETAIL_FIND_SUCCESS = "환불 상세내역 조회 성공";
    public static final String PAYMENT_CANCEL_DETAIL_FIND_FAIL = "환불 상세내역 정보가 존재하지 않습니다.";
}

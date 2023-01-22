package com.safeking.shop.domain.payment.domain.entity;


public enum PaymentStatus {

    READY("READY"), //결제 대기
    PAID("PAID"), // 결제 완료
    CANCEL("CANCEL"), // 결제 취소
    FAILED("FAILED"), // 결제 실패
    ;

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

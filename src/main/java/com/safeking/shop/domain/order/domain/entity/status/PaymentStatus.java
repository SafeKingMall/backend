package com.safeking.shop.domain.order.domain.entity.status;


public enum PaymentStatus {

    COMPLETE("결제 완료"),
    CANCELING("결제 취소 중"),
    CANCEL("결제 취소"),

    ;

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

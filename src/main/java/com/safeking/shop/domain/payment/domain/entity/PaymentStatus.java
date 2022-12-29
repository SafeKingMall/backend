package com.safeking.shop.domain.payment.domain.entity;


public enum PaymentStatus {

    READY("ready"), //결제 대기
    PAID("paid"),
    CANCELLED("canc"),
    FAILED("CANCEL"),
    ;

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

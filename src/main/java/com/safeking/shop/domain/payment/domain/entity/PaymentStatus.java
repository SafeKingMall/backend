package com.safeking.shop.domain.payment.domain.entity;


public enum PaymentStatus {

    COMPLETE("COMPLETE"),
    CANCELING("CANCELING"),
    CANCEL("CANCEL"),
    READY("READY")

    ;

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

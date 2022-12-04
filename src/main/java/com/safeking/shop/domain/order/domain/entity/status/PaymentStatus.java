package com.safeking.shop.domain.order.domain.entity.status;


public enum PaymentStatus {

    COMPLETE("COMPLETE"),
    CANCELING("CANCELING"),
    CANCEL("CANCEL"),

    ;

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

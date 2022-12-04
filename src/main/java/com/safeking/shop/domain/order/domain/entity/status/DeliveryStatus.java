package com.safeking.shop.domain.order.domain.entity.status;

public enum DeliveryStatus {

    PREPARATION("PREPARATION"),
    IN_DELIVERY("IN_DELIVERY"),
    COMPLETE("COMPLETE");

    private final String description;

    DeliveryStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

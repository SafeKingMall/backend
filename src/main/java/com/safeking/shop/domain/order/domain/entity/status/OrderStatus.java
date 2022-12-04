package com.safeking.shop.domain.order.domain.entity.status;


public enum OrderStatus {

    COMPLETE("COMPLETE"),
    CANCEL("CANCEL"),
    ;

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

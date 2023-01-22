package com.safeking.shop.domain.order.domain.entity.status;

public enum DeliveryStatus {

    CANCEL("CANCEL"), // 배송취소
    PREPARATION("PREPARATION"), // 배송대기
    IN_DELIVERY("IN_DELIVERY"), // 배송중
    COMPLETE("COMPLETE"); // 배송완료

    private final String description;

    DeliveryStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

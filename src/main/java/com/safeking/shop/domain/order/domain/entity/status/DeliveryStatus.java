package com.safeking.shop.domain.order.domain.entity.status;

public enum DeliveryStatus {

    PREPARATION("상품 준비"),
    IN_DELIVERY("배송 중"),
    COMPLETE("배송 완료");

    private final String description;

    DeliveryStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

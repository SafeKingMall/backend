package com.safeking.shop.domain.order.domain.entity.status;


public enum OrderStatus {
    READY("READY"), // 주문 접수
    COMPLETE("COMPLETE"), // 주문 완료
    CANCEL("CANCEL"), // 주문 취소
    ;

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

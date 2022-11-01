package com.safeking.shop.domain.order.domain.entity;


public enum OrderStatus {

    // TODO (임시) 필요에 따라 수정하고 주석 제거
    ORDER("주문 대기 상태"),
    COMPLETE("주문 완료 상태"),
    CANCEL("주문 취소 상태"),
    ;

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

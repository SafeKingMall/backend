package com.safeking.shop.domain.order.web.dto.request.user.search;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderSearchCondition {
    private String fromDate;
    private String toDate;
    private String keyword;
    private String deliveryStatus;
    private String paymentStatus;
    private String orderStatus;

    @Builder
    public OrderSearchCondition(String fromDate, String toDate, String keyword, String deliveryStatus, String paymentStats, String orderStatus) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.keyword = keyword;
        this.deliveryStatus = deliveryStatus;
        this.paymentStatus = paymentStats;
        this.orderStatus = orderStatus;
    }
}

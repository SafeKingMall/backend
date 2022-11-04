package com.safeking.shop.domain.order.domain.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class OrderOrderDto {
    private Long memberId;
    private Long itemId;
    private String memo;

    @Builder
    public OrderOrderDto(Long memberId, Long itemId, String memo) {
        this.memberId = memberId;
        this.itemId = itemId;
        this.memo = memo;
    }
}

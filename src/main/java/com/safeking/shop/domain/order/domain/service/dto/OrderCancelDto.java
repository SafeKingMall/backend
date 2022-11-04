package com.safeking.shop.domain.order.domain.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderCancelDto {
    List<Long> ids = new ArrayList<>();

    @Builder
    public OrderCancelDto(List<Long> ids) {
        this.ids = ids;
    }
}

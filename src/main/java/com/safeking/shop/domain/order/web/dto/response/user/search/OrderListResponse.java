package com.safeking.shop.domain.order.web.dto.response.user.search;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderListResponse {
    private String message;
    private List<OrderListOrdersResponse> orders;
    private Long totalElements; // 모든 페이지에 존재하는 총 원소 수
    private Integer size; //
    private Integer totalPages; // 페이지로 제공되는 총 페이지 수

    @Builder
    public OrderListResponse(String message, List<OrderListOrdersResponse> orders, Long totalElements, Integer size, Integer totalPages) {
        this.message = message;
        this.orders = orders;
        this.totalElements = totalElements;
        this.size = size;
        this.totalPages = totalPages;
    }
}

package com.safeking.shop.domain.order.web.dto.response.user.orderitems;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderItemsResponse {
    private String message;
    private List<OrderItemsId> orderItems;

    @Builder
    public OrderItemsResponse(String message, List<OrderItemsId> orderItems) {
        this.message = message;
        this.orderItems = orderItems;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class OrderItemsId {
        private Long id;

        public OrderItemsId(Long id) {
            this.id = id;
        }
    }
}

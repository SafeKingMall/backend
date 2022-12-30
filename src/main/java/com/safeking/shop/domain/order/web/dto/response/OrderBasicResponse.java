package com.safeking.shop.domain.order.web.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderBasicResponse {
    String message;

    public OrderBasicResponse(String message) {
        this.message = message;
    }
}

package com.safeking.shop.domain.order.web.dto.response.user.userdelivery;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserDeliveryResponse {
    private String message;
    private DeliveryResponse delivery;
}

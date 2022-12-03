package com.safeking.shop.domain.order.web.dto.request.admin.modify;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AdminModifyInfoRequest {
    private AdminModifyInfoOrderRequest order;
}

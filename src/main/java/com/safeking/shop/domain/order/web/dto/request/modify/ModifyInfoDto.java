package com.safeking.shop.domain.order.web.dto.request.modify;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.safeking.shop.domain.order.domain.service.dto.order.OrderDto;
import lombok.Data;

@Data
public class ModifyInfoDto {
    @JsonProperty("order")
    private ModifyInfoOrderDto order;
    @JsonProperty("delivery")
    private ModifyInfoDeliveryDto delivery;
}

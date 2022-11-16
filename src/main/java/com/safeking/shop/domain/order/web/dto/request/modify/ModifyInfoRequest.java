package com.safeking.shop.domain.order.web.dto.request.modify;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ModifyInfoRequest {
    @NotNull
    @JsonProperty("order")
    private ModifyInfoOrderRequest order;
    @NotNull
    @JsonProperty("delivery")
    private ModifyInfoDeliveryRequest delivery;
}

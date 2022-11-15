package com.safeking.shop.domain.order.web.dto.request.modify;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ModifyInfoDto {
    @NotNull
    @JsonProperty("order")
    private ModifyInfoOrderDto order;
    @NotNull
    @JsonProperty("delivery")
    private ModifyInfoDeliveryDto delivery;
}

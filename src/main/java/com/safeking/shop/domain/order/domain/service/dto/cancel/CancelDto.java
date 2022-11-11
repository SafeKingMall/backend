package com.safeking.shop.domain.order.domain.service.dto.cancel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CancelDto {
    @JsonProperty("orders")
    List<CancelOrderDtos> cancelOrderDtos;
}

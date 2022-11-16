package com.safeking.shop.domain.order.web.dto.request.cancel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CancelRequest {
    @JsonProperty("orders")
    List<CancelOrderRequest> orders;
}

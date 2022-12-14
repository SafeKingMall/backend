package com.safeking.shop.domain.order.web.dto.request.user.cancel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CancelRequest {
    @NotNull
    @JsonProperty("orders")
    List<CancelOrderRequest> orders;
}

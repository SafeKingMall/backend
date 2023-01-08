package com.safeking.shop.domain.order.web.dto.request.user.cancel;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CancelOrderRequest {
    @NotNull(message = "주문 번호가 필요합니다.")
    private Long id;
    @NotBlank
    private String cancelReason;
}

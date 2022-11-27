package com.safeking.shop.domain.order.web.dto.request.user.cancel;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CancelOrderRequest {
    @NotNull(message = "주문 번호가 필요합니다.")
    private Long id;
}

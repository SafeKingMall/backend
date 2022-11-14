package com.safeking.shop.domain.order.web.dto.request.cancel;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CancelOrderDtos {
    @NotNull(message = "주문 번호가 필요합니다.")
    private Long id;
}

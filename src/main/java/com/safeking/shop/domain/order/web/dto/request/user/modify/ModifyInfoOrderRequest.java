package com.safeking.shop.domain.order.web.dto.request.user.modify;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ModifyInfoOrderRequest {
    @NotNull(message = "주문 번호가 필요합니다.")
    private Long id;
    @NotNull
    private String memo;
}

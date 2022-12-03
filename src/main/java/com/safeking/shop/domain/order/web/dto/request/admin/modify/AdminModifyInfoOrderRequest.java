package com.safeking.shop.domain.order.web.dto.request.admin.modify;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotNull;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AdminModifyInfoOrderRequest {
    private String adminMemo;
    @NotNull
    private AdminModifyInfoDeliveryRequest delivery;
    @NotNull
    private AdminModifyInfoPaymentRequest payment;

}

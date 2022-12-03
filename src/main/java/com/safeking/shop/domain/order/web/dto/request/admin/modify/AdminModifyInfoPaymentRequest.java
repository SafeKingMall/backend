package com.safeking.shop.domain.order.web.dto.request.admin.modify;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AdminModifyInfoPaymentRequest {
    @NotBlank(message = "결제상태가 없습니다.")
    private String status;
}

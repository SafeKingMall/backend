package com.safeking.shop.domain.payment.web.client.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentAuthCancelRequest {
    @NotNull(message = "결제 성공 여부가 빈 값입니다.")
    private Boolean success; // 결제 성공 여부
    @NotBlank(message = "주문 번호가 빈 값 입니다.")
    private String merchantUid; //주문 번호
    private String errorMsg; // 결제실패 메시지
}

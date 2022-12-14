package com.safeking.shop.domain.payment.web.client.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <a herf="https://chai-iamport.gitbook.io/iamport/sdk/javascript-sdk/payrt">결제요청 파라미터</a>
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentCallbackRequest {
    @NotNull(message = "결제 성공 여부가 null 입니다.")
    private Boolean success; // 결제 성공 여부
    @NotBlank(message = "결제 고유 번호가 빈 값입니다.")
    private String impUid; // 결제 고유 번호
    @NotBlank(message = "주문 번호가 빈 값 입니다.")
    private String merchantUid; //주문 번호
    @NotNull(message = "결제 금액이 null 입니다.")
    private Integer paid_amount; // 결제 금액
}

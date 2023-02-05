package com.safeking.shop.domain.payment.web.client.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * <a herf="https://chai-iamport.gitbook.io/iamport/sdk/javascript-sdk/payrt">결제요청 파라미터</a>
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentCallbackRequest {
    @NotNull(message = "결제 성공 여부가 빈 값입니다.")
    private Boolean success; // 결제 성공 여부
    @NotBlank(message = "결제 고유 번호가 빈 값입니다.")
    private String impUid; // 결제 고유 번호
    @NotBlank(message = "주문 번호가 빈 값 입니다.")
    @Size(min = 25, max = 25, message = "주문번호 양식이 맞지 않습니다.") // SFK-230205155215-f62ee0cb
    private String merchantUid; //주문 번호
    @NotNull(message = "결제 금액이 null 입니다.")
    private Integer paidAmount; // 결제 금액
    private String errorMsg; // 결제실패 메시지
}

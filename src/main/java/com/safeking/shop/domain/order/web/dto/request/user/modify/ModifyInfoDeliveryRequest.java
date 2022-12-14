package com.safeking.shop.domain.order.web.dto.request.user.modify;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ModifyInfoDeliveryRequest {
    @NotBlank(message = "받는 사람을 작성해주세요.")
    private String receiver;
    @Pattern(regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$", message = "10 ~ 11 자리의 숫자만 입력 가능합니다.")
    private String phoneNumber;
    @NotBlank(message = "받는 주소를 작성해주세요.")
    private String address;
    @NotNull
    private String memo;
}

package com.safeking.shop.domain.order.web.dto.request.admin.modify;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AdminModifyInfoDeliveryRequest {
    @NotBlank(message = "배송상태가 비어있습니다.")
    private String status;
    @NotBlank(message = "송장번호가 없습니다.")
    private String invoiceNumber;
    @NotNull
    private Integer cost;
    @NotBlank(message = "택배사가 없습니다.")
    private String company;
}

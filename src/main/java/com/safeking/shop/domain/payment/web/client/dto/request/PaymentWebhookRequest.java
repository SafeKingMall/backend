package com.safeking.shop.domain.payment.web.client.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentWebhookRequest {
    @NotNull
    private String impUid;
    @NotNull
    private String merchantUid;
    @NotNull
    private String orderId;
    @NotNull
    private String status;
}

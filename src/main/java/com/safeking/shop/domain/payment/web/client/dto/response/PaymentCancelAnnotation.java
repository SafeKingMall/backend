package com.safeking.shop.domain.payment.web.client.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentCancelAnnotation {
    private String pgTid;
    private Integer amount;
    private String cancelledAt;
    @Length(max = 256)
    private String reason;
    @Length(max = 300)
    private String receiptUrl;
}

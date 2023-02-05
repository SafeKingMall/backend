package com.safeking.shop.domain.order.web.dto.response.user.orderdetail;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderDetailPaymentResponse {
    private String status;
    private String impUid;
    private String cancelReason;
    @Builder
    public OrderDetailPaymentResponse(String status, String impUid, String cancelReason) {
        this.status = status;
        this.impUid = impUid;
        this.cancelReason = cancelReason;
    }
}

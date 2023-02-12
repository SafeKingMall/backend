package com.safeking.shop.domain.payment.web.client.dto.response.cancellist;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentCancelListResponse {

    private String message;
    private Long totalElement;
    private Integer totalPages;
    private Integer size;
    private List<PaymentCancelListOrderResponse> order;

    @Builder
    public PaymentCancelListResponse(String message, Long totalElement, Integer totalPages, Integer size, List<PaymentCancelListOrderResponse> order) {
        this.message = message;
        this.totalElement = totalElement;
        this.totalPages = totalPages;
        this.size = size;
        this.order = order;
    }
}

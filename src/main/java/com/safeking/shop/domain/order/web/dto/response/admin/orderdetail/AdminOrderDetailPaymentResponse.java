package com.safeking.shop.domain.order.web.dto.response.admin.orderdetail;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AdminOrderDetailPaymentResponse {
    private String status;
    private String company;
    private String means;
    private String businessNumber;
    private int price;

    @Builder
    public AdminOrderDetailPaymentResponse(String status, String company, String means, String businessNumber, int price) {
        this.status = status;
        this.company = company;
        this.means = means;
        this.businessNumber = businessNumber;
        this.price = price;
    }
}

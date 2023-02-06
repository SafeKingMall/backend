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
    private int price;
    private String impUid;
    private String buyerName;

    @Builder
    public AdminOrderDetailPaymentResponse(String status, String company, String means, int price, String impUid, String buyerName) {
        this.status = status;
        this.company = company;
        this.means = means;
        this.price = price;
        this.impUid = impUid;
        this.buyerName = buyerName;
    }
}

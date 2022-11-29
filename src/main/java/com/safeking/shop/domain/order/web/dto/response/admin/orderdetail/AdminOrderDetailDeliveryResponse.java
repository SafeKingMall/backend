package com.safeking.shop.domain.order.web.dto.response.admin.orderdetail;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AdminOrderDetailDeliveryResponse {
    private Long id;
    private String status;
    private String receiver;
    private String phoneNumber;
    private String address;
    private String memo;
    private String invoiceNumber;
    private int cost;
    private String company;

    @Builder
    public AdminOrderDetailDeliveryResponse(Long id, String status, String receiver, String phoneNumber, String address, String memo, String invoiceNumber, int cost, String company) {
        this.id = id;
        this.status = status;
        this.receiver = receiver;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.memo = memo;
        this.invoiceNumber = invoiceNumber;
        this.cost = cost;
        this.company = company;
    }
}

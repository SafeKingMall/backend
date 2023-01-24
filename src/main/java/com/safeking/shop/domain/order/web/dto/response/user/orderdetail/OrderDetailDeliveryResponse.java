package com.safeking.shop.domain.order.web.dto.response.user.orderdetail;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderDetailDeliveryResponse {
    private Long id;
    private String status;
    private String receiver;
    private String phoneNumber;
    private String address;
    private String memo;
    private Integer cost;

    @Builder
    public OrderDetailDeliveryResponse(Long id, String status, String receiver, String phoneNumber, String address, String memo, Integer cost) {
        this.id = id;
        this.status = status;
        this.receiver = receiver;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.memo = memo;
        this.cost = cost;
    }
}

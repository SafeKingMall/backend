package com.safeking.shop.domain.order.web.dto.response.user.orderinfo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderInfoDeliveryResponse {
    private String receiver;
    private String phoneNumber;
    private String address;
    private String memo;

    @Builder
    public OrderInfoDeliveryResponse(String receiver, String phoneNumber, String address, String memo) {
        this.receiver = receiver;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.memo = memo;
    }
}

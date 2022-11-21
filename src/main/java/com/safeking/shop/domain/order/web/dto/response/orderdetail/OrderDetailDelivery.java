package com.safeking.shop.domain.order.web.dto.response.orderdetail;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderDetailDelivery {
    private Long id;
    private String status;
    private String receiver;
    private String phoneNumber;
    private String address;
    private String memo;

    @Builder
    public OrderDetailDelivery(Long id, String status, String receiver, String phoneNumber, String address, String memo) {
        this.id = id;
        this.status = status;
        this.receiver = receiver;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.memo = memo;
    }
}

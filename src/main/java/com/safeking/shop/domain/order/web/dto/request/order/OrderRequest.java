package com.safeking.shop.domain.order.web.dto.request.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderRequest {
    private String receiver;
    private String phoneNumber;
    private String address;
    private String memo;
    @JsonProperty("items")
    private List<OrderItemRequest> orderItemRequests;
    @JsonProperty("delivery")
    private OrderDeliveryRequest orderDeliveryRequest;

    public OrderRequest(String receiver, String phoneNumber, String address, String memo, List<OrderItemRequest> orderItemRequests, OrderDeliveryRequest orderDeliveryRequest) {
        this.receiver = receiver;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.memo = memo;
        this.orderItemRequests = orderItemRequests;
        this.orderDeliveryRequest = orderDeliveryRequest;
    }
}

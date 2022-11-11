package com.safeking.shop.domain.order.domain.service.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderDto {
    private String receiver;
    private String phoneNumber;
    private String address;
    private String memo;
    @JsonProperty("items")
    private List<OrderItemDto> orderItemDtos;
    @JsonProperty("delivery")
    private OrderDeliveryDto orderDeliveryDto;

    public OrderDto(String receiver, String phoneNumber, String address, String memo, List<OrderItemDto> orderItemDtos, OrderDeliveryDto orderDeliveryDto) {
        this.receiver = receiver;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.memo = memo;
        this.orderItemDtos = orderItemDtos;
        this.orderDeliveryDto = orderDeliveryDto;
    }
}

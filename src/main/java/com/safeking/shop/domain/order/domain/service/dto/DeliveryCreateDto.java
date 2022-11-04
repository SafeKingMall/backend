package com.safeking.shop.domain.order.domain.service.dto;

import com.safeking.shop.domain.order.domain.entity.status.DeliveryStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DeliveryCreateDto {
    private String receiver;
    private String phoneNumber;
    private String address;
    private DeliveryStatus status;
    private LocalDateTime shippingStartDate;
    private LocalDateTime shippingEndDate;

    @Builder
    public DeliveryCreateDto(String receiver, String phoneNumber, String address, DeliveryStatus status, LocalDateTime shippingStartDate, LocalDateTime shippingEndDate) {
        this.receiver = receiver;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.status = status;
        this.shippingStartDate = shippingStartDate;
        this.shippingEndDate = shippingEndDate;
    }
}

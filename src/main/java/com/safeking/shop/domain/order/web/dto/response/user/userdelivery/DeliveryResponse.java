package com.safeking.shop.domain.order.web.dto.response.user.userdelivery;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DeliveryResponse {
    private String receiver;
    private String email;
    private String phoneNumber;
    private String address;
    private String detailAddress;
    private String zipcode;

    @Builder
    public DeliveryResponse(String receiver, String email, String phoneNumber, String address, String detailAddress, String zipcode) {
        this.receiver = receiver;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.detailAddress = detailAddress;
        this.zipcode = zipcode;
    }
}

package com.safeking.shop.domain.order.web.dto.request.modify;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ModifyInfoDeliveryDto {
    private String receiver;
    private String phoneNumber;
    private String address;
    private String memo;
}

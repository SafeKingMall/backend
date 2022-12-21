package com.safeking.shop.domain.payment.web.client.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IamportResponse<T> {
    private int code;
    private String message;
    private T response;
}

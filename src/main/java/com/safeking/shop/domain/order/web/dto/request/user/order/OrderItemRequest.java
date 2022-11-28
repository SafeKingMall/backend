package com.safeking.shop.domain.order.web.dto.request.user.order;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderItemRequest {
    @NotNull
    private Long id;
    @Range(min = 1, message = "수량은 최소 1개 이상 이어야 합니다.")
    private int count;

    public OrderItemRequest(Long id, int count) {
        this.id = id;
        this.count = count;
    }
}

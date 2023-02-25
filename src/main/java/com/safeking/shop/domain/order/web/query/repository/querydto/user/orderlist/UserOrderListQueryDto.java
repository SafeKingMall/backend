package com.safeking.shop.domain.order.web.query.repository.querydto.user.orderlist;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(of = "id")
public class UserOrderListQueryDto {
    private Long id;
    private String status;
    private int price;
    private LocalDateTime date;
    private String merchantUid;
    private List<UserOrderListOrderItemQueryDto> orderItems;
    private UserOrderListPaymentQueryDto payment;
    private UserOrderListDeliveryQueryDto delivery;

    @QueryProjection
    public UserOrderListQueryDto(Long id, String status, int price,
                                 LocalDateTime date, String merchantUid,
                                 UserOrderListPaymentQueryDto payment,
                                 UserOrderListDeliveryQueryDto delivery) {
        this.id = id;
        this.status = status;
        this.price = price;
        this.date = date;
        this.merchantUid = merchantUid;
        this.payment = payment;
        this.delivery = delivery;
    }
}

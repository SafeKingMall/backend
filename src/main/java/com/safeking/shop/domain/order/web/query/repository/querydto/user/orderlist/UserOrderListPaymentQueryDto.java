package com.safeking.shop.domain.order.web.query.repository.querydto.user.orderlist;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserOrderListPaymentQueryDto {
    private String status;
    private Date paidDate;
    private LocalDateTime canceledDate;

    @QueryProjection
    public UserOrderListPaymentQueryDto(String status, Date paidDate, LocalDateTime canceledDate) {
        this.status = status;
        this.paidDate = paidDate;
        this.canceledDate = canceledDate;
    }
}

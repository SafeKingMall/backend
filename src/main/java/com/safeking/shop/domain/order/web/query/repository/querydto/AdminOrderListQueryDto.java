package com.safeking.shop.domain.order.web.query.repository.querydto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@EqualsAndHashCode(of = "id")
public class AdminOrderListQueryDto {
    private Long id;
    private String status;
    private int price;
    private LocalDateTime date;
    private int orderItemCount;
    private List<AdminOrderListOrderItemQueryDto> orderItems;
    private AdminOrderListPaymentQueryDto payment;
    private AdminOrderListMemberQueryDto member;
    private AdminOrderListDeliveryQueryDto delivery;
    private String merchantUid;

    @QueryProjection
    public AdminOrderListQueryDto(Long id, String status, int price, LocalDateTime date, String merchantUid,
                                  AdminOrderListPaymentQueryDto payment,
                                  AdminOrderListMemberQueryDto member,
                                  AdminOrderListDeliveryQueryDto delivery) {
        this.id = id;
        this.status = status;
        this.price = price;
        this.date = date;
        this.payment = payment;
        this.member = member;
        this.delivery = delivery;
        this.merchantUid = merchantUid;
    }
}

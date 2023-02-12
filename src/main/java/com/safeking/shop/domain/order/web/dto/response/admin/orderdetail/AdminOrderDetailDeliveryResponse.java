package com.safeking.shop.domain.order.web.dto.response.admin.orderdetail;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AdminOrderDetailDeliveryResponse {
    private Long id;
    private String status; // 배송 상태
    private String receiver; // 수령인
    private String phoneNumber; // 수령인 연락처
    private String address; // 수령인 주소
    private String memo; // 배송 요청사항
    private String invoiceNumber; // 송장번호
    private int cost; // 배송료
    private String company; // 택배사

    @Builder
    public AdminOrderDetailDeliveryResponse(Long id, String status, String receiver, String phoneNumber, String address, String memo, String invoiceNumber, int cost, String company) {
        this.id = id;
        this.status = status;
        this.receiver = receiver;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.memo = memo;
        this.invoiceNumber = invoiceNumber;
        this.cost = cost;
        this.company = company;
    }
}

package com.safeking.shop.domain.order.web.dto.response.user.orderdetail;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderDetailDeliveryResponse {
    private String status; // 배송 상태
    private String receiver; // 수령인
    private String phoneNumber; // 연락처
    private String address; // 수령인 주소
    private String memo; // 배송요청사항
    private Integer cost; // 배송료
    private String company; // 택배사
    private String invoiceNumber; // 송장번호

    @Builder
    public OrderDetailDeliveryResponse(String status, String receiver, String phoneNumber, String address, String memo, Integer cost,
                                       String company, String invoiceNumber) {
        this.status = status;
        this.receiver = receiver;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.memo = memo;
        this.cost = cost;
        this.company = company;
        this.invoiceNumber = invoiceNumber;
    }
}

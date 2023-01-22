package com.safeking.shop.domain.order.web.dto.request.user.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderRequest {
    @NotBlank(message = "받는 사람 이름을 입력해주세요.")
    private String receiver;
    @NotBlank
    @Email(message = "이메일 형식을 준수해주세요.")
    private String email;
    @Pattern(regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$", message = "10 ~ 11 자리의 숫자만 입력 가능합니다.")
    private String phoneNumber;
    @NotBlank(message = "주소를 입력해주세요.")
    private String address;
    @NotNull
    private String memo;
    @NotNull
    private String deliveryMemo;
    @JsonProperty("items")
    @NotNull
    private List<OrderItemRequest> orderItemRequests;
    private String merchantUid;

    public OrderRequest(String receiver, String email, String phoneNumber, String address, String memo, List<OrderItemRequest> orderItemRequests, String deliveryMemo, String merchantUid) {
        this.receiver = receiver;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.memo = memo;
        this.orderItemRequests = orderItemRequests;
        this.deliveryMemo = deliveryMemo;
        this.merchantUid = merchantUid;
    }
}

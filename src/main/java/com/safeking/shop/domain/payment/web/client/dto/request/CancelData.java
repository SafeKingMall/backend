package com.safeking.shop.domain.payment.web.client.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CancelData {
    @Length(max = 32)
    @NotBlank
    private String impUid; // 아임포트 거래고유번호
    @Length(max = 40)
    @NotBlank
    private String merchantUid; //주문번호

    private Double amount; //취소 요청 금액
    private Double taxFree; //취소요청금액 중 면세금액
    private Integer vatAmount; //부가세 지정
    @Length(max = 16)
    private String refundTel; //환불계좌 예금주 연락처
    private Double checkSum; //현재시점의 취소 가능한 잔액
    @Length(max = 256)
    private String reason; //취소사유
    @Length(max = 16)
    private String refundHolder; // 환불계좌 예금주
    @Length(max = 4)
    private String refundBank; //환불계좌 은행코드
    @Length(max = 16)
    private String refundAccount; //환불계좌 계좌번호
}

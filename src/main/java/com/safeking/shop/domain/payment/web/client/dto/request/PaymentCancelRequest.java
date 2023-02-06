package com.safeking.shop.domain.payment.web.client.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentCancelRequest {
    @NotBlank(message = "결제 고유 번호가 빈 값입니다.")
    private String impUid; // 결제 고유 번호
    @NotBlank(message = "주문 번호가 빈 값 입니다.")
    private String merchantUid; //주문 번호
    @NotNull(message = "반품비가 빈 값 입니다.")
    private Double returnFee; // 반품비
    private String reason; // 취소사유

//    private Double amount; // 취소 요청금액(누락시 전액 취소)
//    private Double taxFree; // 취소요청금액 중 면세금액 (누락되면 0원처리)
//    /**
//     * 부가세 지정(기본값: null)
//     * 결제 시 부가세를 지정했던 경우 필수 입력 바랍니다.
//     *
//     * 지원 PG사
//     * -나이스페이먼츠
//     * -이니시스
//     */
//    private Integer vatAmount;
//    private String refundTel; // 환불계좌 예금주 연락처(가상계좌 취소,스마트로 PG사 인경우 필수 )
//    private String checksum; // 현재시점의 취소 가능한 잔액.
//    private String refundHolder; // 환불계좌 예금주 (가상계좌 취소시 필수)
//    private String refundBank; // 환불계좌 은행코드 (하단 은행코드표 참조, 가상계좌 취소시 필수)
//    private String refundAccount; // 환불계좌 계좌번호 (가상계좌 취소시 필수)
}

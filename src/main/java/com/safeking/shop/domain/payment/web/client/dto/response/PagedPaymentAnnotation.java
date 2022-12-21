package com.safeking.shop.domain.payment.web.client.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PagedPaymentAnnotation {
    @Length(max = 32)
    private String impUid;
    @Length(max = 40)
    private String merchantUid;
    @Length(max = 20)
    private String payMethod;
    @Length(max = 10)
    private String channel;
    @Length(max = 16)
    private String pgProvider;
    @Length(max = 16)
    private String embPgProvider;
    @Length(max = 80)
    private String pgTid;
    @Length(max = 80)
    private String pgId;
    private boolean escrow;
    @Length(max = 20)
    private String applyNum;
    @Length(max = 4)
    private String bankCode;
    @Length(max = 20)
    private String bankName;
    @Length(max = 3)
    private String cardCode;
    @Length(max = 20)
    private String cardName;
    private Integer cardQuota;
    @Length(max = 20)
    private String cardNumber;
    @Length(max = 2)
    private String cardType;
    @Length(max = 4)
    private String vbankCode;
    @Length(max = 20)
    private String vbankName;
    @Length(max = 16)
    private String vbankHolder;
    private String vbankDate;
    private String vbankIssedAt;
    @Length(max = 40)
    private String name;
    private Integer amount; //누락시 전액 취소
    private Integer cancelAmount;
    @Length(max = 3)
    private String currency;
    @Length(max = 16)
    private String buyerName;
    @Length(max = 64)
    private String buyerEmail;
    @Length(max = 16)
    private String buyerTel;
    @Length(max = 128)
    private String buyerAddr;
    @Length(max = 8)
    private String buyerPostcode;
    private String customData;
    @Length(max = 256)
    private String userAgent;
    @Length(max = 20)
    private String status;
    private String startedAt;
    private String paidAt;
    private String failedAt;
    private String cancelledAt;
    @Length(max = 256)
    private String failReason;
    @Length(max = 256)
    private String cancelReason;
    @Length(max = 300)
    private String receiptUrl;
    private boolean cashReceiptIssued;
    @Length(max = 80)
    private String customerUid;
    @Length(max = 20)
    private String customerUidUsage;
    private List<PaymentCancelAnnotation> cancelHistory;
}

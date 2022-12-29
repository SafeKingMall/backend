package com.safeking.shop.domain.payment.domain.entity;

import com.safeking.shop.domain.common.BaseTimeEntity;
import com.safeking.shop.domain.order.domain.entity.OrderItem;
import com.siot.IamportRestClient.response.Payment;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.safeking.shop.domain.order.constant.OrderConst.DeliveryCost;
import static com.safeking.shop.domain.payment.domain.entity.PaymentStatus.*;

/**
 * 금액은 Integer로 선언(21억까지 가능)
 */
@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SafekingPayment extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "safeking_payment_id")
    private Long id;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    // 아임포트 결제 단건 조회 응답 시작
    private String impUid; // 아임포트 결제 고유 번호
    private String merchantUid; //가맹점에서 전달한 거래 고유 번호
    private String payMethod; // 결제 방식
    private String channel; // 결제가 발생된 경로 [pc, mobile, api]
    private String pgProvider; // pg사 명칭
    private String pgTid; // pg사 승인정보
    private String pgId; // 거래가 처리된 pg사 상점 아이디
    private boolean escrow; // 에스크로결제 여부
    private String applyNum; // 카드사 승인정보(계좌이체/가상계좌는 값 없음)
    private String bankCode;// 은행 표준코드 - (금융결제원기준)
    private String bankName; // 은행 명칭 - (실시간계좌이체 결제 건의 경우)
    private String cardCode; // 카드사 코드번호
    private String cardQuota; // 할부개월 수(0이면 일시불)
    private String cardNumber; // 결제에 사용된 마스킹된 카드번호. 7~12번째 자리를 마스킹하는 것이 일반적이지만, PG사의 정책/설정에 따라 다소 차이가 있을 수 있음
    private String cardType; // 카드유형. (주의)해당 정보를 제공하지 않는 일부 PG사의 경우 null 로 응답됨(ex. JTNet, 이니시스-빌링) = ['null', '0(신용카드)', '1(체크카드)']
    private String vbankCode; // 가상계좌 은행 표준코드 - (금융결제원기준)
    private String vbankName; // 입금받을 가상계좌 은행명
    private String vbankNum; // 입금받을 가상계좌 계좌번호
    private String vbankHolder; // 입금받을 가상계좌 예금주
    private String vbankDate; // 입금받을 가상계좌 마감기한 UNIX timestamp
    private String vbankIssuedAt; // 가상계좌 생성 시각 UNIX timestamp
    private String name; // 주문명칭
    private Integer amount; // 주문(결제)금액
    private Integer cancelAmount; // 결제취소금액
    private String currency; // 결제승인화폐단위(KRW:원, USD:미화달러, EUR:유로)
    private String buyerName; // 구매자 이름
    private String buyerEmail; // 구매자 이메일
    private String buyerTel; // 구매자 휴대폰
    private String buyerAddr; // 구매자 주소
    private String buyerPostcode; // 구매자 우편 번호
    @Lob
    private String customData; // 가맹점에서 전달한 custom data. JSON string으로 전달
    private String userAgent;
//    private String status; // -> PaymentStatus status로 대체
    private Long startedAt; // 결제시작시점 UNIX timestamp. IMP.request_pay() 를 통해 결제창을 최초 오픈한 시각
    private Date paidAt; // 결제완료시점 UNIX timestamp. 결제완료가 아닐 경우 0
    private Long failedAt; // 결제실패시점 UNIX timestamp. 결제실패가 아닐 경우 0
    private Long cancelledAt; // 결제취소시점 UNIX timestamp. 결제취소가 아닐 경우 0
    private String failReason; // 결제실패 사유
    private String cancelReason; // 결제취소 사유
    private String receiptUrl; // 신용카드 매출전표 확인 URL
//    private List<CancellHistory>; // 취소/부분취소 내역 -> 부분 취소 기능 없음
//    private String cancelReceiptUrls; // Deprecated : cancel_history 사용 권장) 취소/부분취소 시 생성되는 취소 매출전표 확인 URL. 부분취소 횟수만큼 매출전표가 별도로 생성됨
//    private String cashReceiptIssued; // 현금영수증 자동발급 여부
    private String customerUid; // 해당 결제처리에 사용된 customer_uid. 결제창을 통해 빌링키 발급 성공한 결제건의 경우 요청된 customer_uid 값을 응답합니다.
    private String customerUidUsage; // customer_uid가 결제처리에 사용된 상세 용도.(null:일반결제, issue:빌링키 발급, payment:결제, payment.scheduled:예약결제 = ['issue', 'payment', 'payment.scheduled']
    // 아임포트 단건 결제 조회 응답 끝

    public static SafekingPayment createPayment(List<OrderItem> orderItems, String merchantUid, String payMethod) {

        SafekingPayment safeKingPayment = new SafekingPayment();
        int totalItemsPrice = safeKingPayment.sumTotalItemsPrice(orderItems); //총 상품 금액
        int totalPaymentPrice = safeKingPayment.sumTotalPaymentPrice(totalItemsPrice); //총 결제 금액(총 상품 금액 + 배송비)

        safeKingPayment.changeSafekingPayment(totalPaymentPrice, merchantUid, payMethod);

        return safeKingPayment;
    }

    private void changeSafekingPayment(int amount, String merchantUid, String payMethod) {
        this.amount = amount;
        this.status = READY;
        this.merchantUid = merchantUid;
        this.payMethod = payMethod;
    }

    /**
     * 주문시 총 상품 가격 계산
     */
    private int sumTotalItemsPrice(List<OrderItem> orderItems) {
        return orderItems.stream()
                .mapToInt(o -> o.getOrderPrice() * o.getCount())
                .sum();
    }

    /**
     * 주문시 총 결제 가격 계산
     */
    private int sumTotalPaymentPrice(int sumTotalItemsPrice) {
        return sumTotalItemsPrice + DeliveryCost;
    }

    public void changePaymentStatusByAdmin(String status) {
        this.status = valueOf(status);
    }

    public void changePaymentStatus(PaymentStatus status) {
        this.status = status;
    }

    public void changeSafekingPayment(PaymentStatus status, Payment response) {
        this.status = status;
        this.impUid = response.getImpUid();
        this.merchantUid = response.getMerchantUid();
        this.payMethod = response.getPayMethod();
        this.channel = response.getChannel();
        this.pgProvider = response.getPgProvider();
        this.pgTid = response.getPgTid();
//        this.pgId = pgId;
        this.escrow = response.isEscrow();
        this.applyNum = response.getApplyNum();
        this.bankCode = response.getBankCode();
        this.bankName = response.getBankName();
        this.cardCode = response.getCardCode();
        this.cardQuota = response.getCardCode();
        this.cardNumber = response.getCardNumber();
        this.cardType = response.getCardCode();
        this.vbankCode = response.getVbankCode();
        this.vbankName = response.getVbankName();
        this.vbankNum = response.getVbankNum();
        this.vbankHolder = response.getVbankHolder();
        this.vbankDate = response.getVbankCode();
        this.vbankIssuedAt = response.getVbankCode();
        this.name = response.getName();
        this.amount = response.getAmount().intValue();
        this.cancelAmount = response.getCancelAmount().intValue();
        this.currency = response.getCurrency();
        this.buyerName = response.getBuyerName();
        this.buyerEmail = response.getBuyerEmail();
        this.buyerTel = response.getBuyerTel();
        this.buyerAddr = response.getBuyerAddr();
        this.buyerPostcode = response.getBuyerPostcode();
        this.customData = response.getCustomData();
        this.userAgent = response.getApplyNum();
        this.startedAt = response.getStartedAt();
        this.paidAt = response.getPaidAt();
        this.failedAt = response.getStartedAt();
        this.cancelledAt = response.getStartedAt();
        this.failReason = response.getFailReason();
        this.cancelReason = response.getCancelReason();
        this.receiptUrl = response.getReceiptUrl();
        this.customerUid = response.getCustomerUid();
        this.customerUidUsage = response.getCustomerUidUsage();
    }
}

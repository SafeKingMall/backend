package com.safeking.shop.domain.payment.domain.entity;

import com.safeking.shop.domain.common.BaseTimeEntity;
import com.safeking.shop.domain.order.domain.entity.OrderItem;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

import static com.safeking.shop.domain.payment.domain.entity.PaymentStatus.*;
import static com.safeking.shop.domain.order.constant.OrderConst.deliveryCost;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SafekingPayment extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "safeking_payment_id")
    private Long id;
    private String pg; // pg사
    private String merchantUid; //주문 번호
    private String payMethod; // 결제 방식
    private String name; // 상품명
    private Integer amount; // 결제 금액
    private String buyerEmail; // 구매자 이메일
    private String buyerName; // 구매자 이름
    private String buyerTel; // 구매자 휴대폰
    private String buyerAddr; // 구매자 주소
    private String buyerPostcode; // 구매자 우편 번호
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private String company;
    private String businessNumber;

    public static SafekingPayment createPayment(List<OrderItem> orderItems, String number, String means) {

        SafekingPayment safeKingPayment = new SafekingPayment();
        int totalItemsPrice = safeKingPayment.sumTotalItemsPrice(orderItems); //총 상품 가격
        int totalPaymentPrice = safeKingPayment.sumTotalPaymentPrice(totalItemsPrice); //총 결제 가격

        safeKingPayment.changePayment(totalPaymentPrice, number, means);

        return safeKingPayment;
    }

    private void changePayment(int amount, String merchantUid, String payMethod) {
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
        return sumTotalItemsPrice + deliveryCost;
    }

    public void changePaymentStatusByAdmin(String status) {
        this.status = valueOf(status);
    }
}

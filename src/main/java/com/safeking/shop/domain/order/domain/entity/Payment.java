package com.safeking.shop.domain.order.domain.entity;

import com.safeking.shop.domain.common.BaseTimeEntity;
import com.safeking.shop.domain.order.domain.entity.status.PaymentStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

import static com.safeking.shop.domain.order.domain.entity.status.PaymentStatus.*;
import static com.safeking.shop.domain.order.web.OrderConst.deliveryCost;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;
    private Integer price;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private String number;
    private String means;
    private String company;
    private String businessNumber;

    public static Payment createPayment(List<OrderItem> orderItems, String number, String means) {

        Payment payment = new Payment();
        int totalItemsPrice = payment.sumTotalItemsPrice(orderItems); //총 상품 가격
        int totalPaymentPrice = payment.sumTotalPaymentPrice(totalItemsPrice); //총 결제 가격

        payment.changePayment(totalPaymentPrice, number, means);

        return payment;
    }

    private void changePayment(int price, String number, String means) {
        this.price = price;
        this.status = COMPLETE;
        this.number = number;
        this.means = means;
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

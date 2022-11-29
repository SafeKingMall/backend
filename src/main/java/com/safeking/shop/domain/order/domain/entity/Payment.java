package com.safeking.shop.domain.order.domain.entity;

import com.safeking.shop.domain.common.BaseTimeEntity;
import com.safeking.shop.domain.order.domain.entity.status.PaymentStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;
    private int price;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private String number;
    private String means;
    private String company;
    private String businessNumber;

    public static Payment createPayment(List<OrderItem> orderItems, String number, String means) {
        Payment payment = new Payment();
        payment.changePayment(payment.sumPrice(orderItems), PaymentStatus.COMPLETE, number, means);

        return payment;
    }

    private void changePayment(int price, PaymentStatus status, String number, String means) {
        this.price = price;
        this.status = status;
        this.number = number;
        this.means = means;
    }

    /**
     * 주문시 총 가격 계산
     */
    private int sumPrice(List<OrderItem> orderItems) {
        return orderItems.stream()
                .mapToInt(o -> o.getOrderPrice() * o.getCount())
                .sum();
    }
}

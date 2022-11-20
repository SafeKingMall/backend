package com.safeking.shop.domain.order.domain.entity;

import com.safeking.shop.domain.admin.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    private int price;
    private String status;
    private String number;
    private String means;

    public static Payment createPayment(Long id, Order order, int price, String status, String number, String means) {
        Payment payment = new Payment();
        payment.changePayment(id, order, price, status, number, means, payment);

        return payment;
    }

    private void changePayment(Long id, Order order, int price, String status, String number, String means, Payment payment) {
        payment.id = id;
        payment.order = order;
        payment.price = price;
        payment.status = status;
        payment.number = number;
        payment.means = means;
    }
}

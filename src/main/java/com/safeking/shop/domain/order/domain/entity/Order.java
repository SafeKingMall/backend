package com.safeking.shop.domain.order.domain.entity;

import com.safeking.shop.domain.common.BaseTimeEntity;
import com.safeking.shop.domain.order.domain.entity.status.OrderStatus;
import com.safeking.shop.domain.user.domain.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity @Getter
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Lob
    @Column(length = 50)
    private String memo;

    public static Order createOrder(Member member, Delivery delivery, OrderStatus status, String memo) {
        Order order = new Order();
        order.changeOrder(member, delivery, status, memo);

        return order;
    }

    public void changeOrder(Member member, Delivery delivery, OrderStatus status, String memo) {
        this.member = member;
        this.delivery = delivery;
        this.status = status;
        this.memo = memo;
    }
}

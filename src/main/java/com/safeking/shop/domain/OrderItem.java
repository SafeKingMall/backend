package com.safeking.shop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderitem_id", nullable = false)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    private int orderPrice;
    private int count;
}

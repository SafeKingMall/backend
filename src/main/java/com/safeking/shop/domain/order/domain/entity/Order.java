package com.safeking.shop.domain.order.domain.entity;

import com.safeking.shop.domain.common.BaseTimeEntity;
import com.safeking.shop.domain.exception.OrderException;
import com.safeking.shop.domain.order.domain.entity.status.DeliveryStatus;
import com.safeking.shop.domain.order.domain.entity.status.OrderStatus;
import com.safeking.shop.domain.order.constant.OrderConst;
import com.safeking.shop.domain.payment.domain.entity.SafekingPayment;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * 주문과 주문상품은 일대다 양방향관계로 설정
     * cascade 를 설정하여 order 를 persist 할 때, orderItems 도 persist 된다.
     */
    @OneToMany(mappedBy = "order"
            , cascade = CascadeType.ALL
            , orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Lob
    @Column(length = 50)
    private String memo;
    @Lob
    private String adminMemo;

    @OneToOne
    @JoinColumn(name = "safeking_payment_id")
    private SafekingPayment safeKingPayment;

    private String merchantUid; // 가맹점에서 전달한 고유 번호
    @Lob
    private String cancelReason; // 주문 취소 사유

    public static Order createOrder(Member member, Delivery delivery, String memo, String merchantUid, SafekingPayment safeKingPayment, List<OrderItem> orderItems) {
        Order order = new Order();
        order.changeOrder(member, delivery, memo, merchantUid, safeKingPayment, orderItems);

        return order;
    }

    public void changeOrder(Member member, Delivery delivery, String memo, String merchantUid, SafekingPayment safeKingPayment, List<OrderItem> orderItems) {
        this.member = member;
        this.delivery = delivery;
        this.status = OrderStatus.READY;
        this.memo = memo;
        this.safeKingPayment = safeKingPayment;
        this.merchantUid = merchantUid;

        for(OrderItem orderItem : orderItems) {
            changeOrderItem(orderItem);
        }
    }

    private void changeOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.changeOrder(this);
    }

    /**
     * 주문 취소
     */
    public void cancel(String cancelReason) {
        if(delivery.getStatus().equals(DeliveryStatus.COMPLETE)
                || delivery.getStatus().equals(DeliveryStatus.IN_DELIVERY)) {
            throw new OrderException(OrderConst.ORDER_CANCEL_DELIVERY_DONE);
        }

        this.status = OrderStatus.CANCEL;
        this.cancelReason = cancelReason;

        //주문상품을 취소
        orderItems.forEach(OrderItem::cancel);
    }

    public void changeMemo(String memo) {
        this.memo = memo;
    }

    public void changeSafekingPayment(SafekingPayment safeKingPayment) {
        this.safeKingPayment = safeKingPayment;
    }

    public void changeAdminMemoByAdmin(String adminMemo) {
        this.adminMemo = adminMemo;
    }

    public void changeMerchantUid(String merchantUid) {
        this.merchantUid = merchantUid;
    }
}

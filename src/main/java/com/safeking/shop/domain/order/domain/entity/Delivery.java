package com.safeking.shop.domain.order.domain.entity;

import com.safeking.shop.domain.admin.common.BaseTimeEntity;
import com.safeking.shop.domain.order.domain.entity.status.DeliveryStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Long id;
    private String receiver;
    @Column(length = 11)
    private String phoneNumber;
    private String address;
    private DeliveryStatus status;
    private LocalDateTime shippingStartDate;
    private LocalDateTime shippingEndDate;

    public static Delivery createDelivery(String receiver, String phoneNumber,
                                          String address, DeliveryStatus status,
                                          LocalDateTime shippingStartDate, LocalDateTime shippingEndDate) {
        Delivery delivery = new Delivery();
        delivery.changeDelivery(receiver, phoneNumber, address, status, shippingStartDate, shippingEndDate);

        return delivery;
    }

    public void changeDelivery(String receiver, String phoneNumber,
                               String address, DeliveryStatus status,
                               LocalDateTime shippingStartDate, LocalDateTime shippingEndDate) {
        this.receiver = receiver;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.status = status;
        this.shippingStartDate = shippingStartDate;
        this.shippingEndDate = shippingEndDate;
    }
}

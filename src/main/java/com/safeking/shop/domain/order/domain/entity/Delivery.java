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
    private String memo;
    private LocalDateTime shippingStartDate;
    private LocalDateTime shippingEndDate;
    private int cost;
    private String company;
    private String invoiceNumber;

    public static Delivery createDelivery(String receiver, String phoneNumber,
                                          String address, DeliveryStatus status,
                                          String memo) {
        Delivery delivery = new Delivery();
        delivery.changeDelivery(receiver, phoneNumber, address, status, memo, LocalDateTime.now());

        return delivery;
    }

    public void changeDelivery(String receiver, String phoneNumber,
                               String address, DeliveryStatus status,
                               String memo,
                               LocalDateTime shippingStartDate) {
        this.receiver = receiver;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.status = status;
        this.memo = memo;
        this.shippingStartDate = shippingStartDate;
    }

    public void changeDeliveryStatus(DeliveryStatus status) {
        this.status = status;
    }

    public void changeDelivery(String receiver, String phoneNumber, String address, String memo) {
        this.receiver = receiver;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.memo = memo;
    }

    public void changeDeliveryByAdmin(int cost, String company, String invoiceNumber) {
        this.cost = cost;
        this.company = company;
        this.invoiceNumber = invoiceNumber;
    }
}

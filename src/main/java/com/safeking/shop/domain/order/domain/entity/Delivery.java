package com.safeking.shop.domain.order.domain.entity;

import com.safeking.shop.domain.common.BaseTimeEntity;
import com.safeking.shop.domain.order.domain.entity.status.DeliveryStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.safeking.shop.domain.order.constant.OrderConst.DeliveryCost;

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
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;
    private String memo;
    private LocalDateTime shippingStartDate;
    private LocalDateTime shippingEndDate;
    private Integer cost;
    private String company;
    private String invoiceNumber;
    private String email;

    public static Delivery createDelivery(String receiver, String phoneNumber,
                                          String address, DeliveryStatus status,
                                          String memo, String email) {
        Delivery delivery = new Delivery();
        delivery.changeDelivery(receiver, phoneNumber, address, status, memo, LocalDateTime.now(), email);

        return delivery;
    }

    private void changeDelivery(String receiver, String phoneNumber,
                               String address, DeliveryStatus status,
                               String memo, LocalDateTime shippingStartDate,
                               String email) {
        this.receiver = receiver;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.status = status;
        this.memo = memo;
        this.shippingStartDate = shippingStartDate;
        this.cost = DeliveryCost;
        this.email = email;
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

    public void changeDeliveryByAdmin(String status, int cost, String company, String invoiceNumber) {
        this.status = DeliveryStatus.valueOf(status);
        this.cost = cost;
        this.company = company;
        this.invoiceNumber = invoiceNumber;
    }

    public void changeDeliveryEmail() {

    }
}

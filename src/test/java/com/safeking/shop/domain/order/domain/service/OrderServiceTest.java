package com.safeking.shop.domain.order.domain.service;

import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.item.domain.repository.ItemRepository;
import com.safeking.shop.domain.order.domain.entity.Delivery;
import com.safeking.shop.domain.order.domain.entity.Order;
import com.safeking.shop.domain.order.domain.entity.OrderItem;
import com.safeking.shop.domain.order.domain.entity.status.DeliveryStatus;
import com.safeking.shop.domain.order.domain.repository.DeliveryRepository;
import com.safeking.shop.domain.order.domain.repository.OrderItemRepository;
import com.safeking.shop.domain.order.domain.repository.OrderRepository;
import com.safeking.shop.domain.payment.domain.entity.SafekingPayment;
import com.safeking.shop.domain.payment.domain.repository.SafekingPaymentRepository;
import com.safeking.shop.domain.user.domain.entity.member.GeneralMember;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class OrderServiceTest {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderItemRepository orderItemRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    DeliveryRepository deliveryRepository;
    @Autowired
    SafekingPaymentRepository paymentRepository;
    @Autowired
    OrderServiceImpl orderService;
    @Autowired
    EntityManager em;

    @Test
    public void deleteByMemberBatch() {
        //given
        // member create
        GeneralMember member = GeneralMember.builder().build();
        GeneralMember savedMember = memberRepository.save(member);

        GeneralMember member2 = GeneralMember.builder().build();
        GeneralMember savedMember2 = memberRepository.save(member2);

        // item create
        Item savedItem1 = itemRepository.save(getItem());
        Item savedItem2 = itemRepository.save(getItem());
        Item savedItem3 = itemRepository.save(getItem());

        OrderItem orderItem = OrderItem
                .createOrderItem(savedItem1, 1001, 10, null);

        OrderItem orderItem2 = OrderItem
                .createOrderItem(savedItem2, 1002, 10, null);

        OrderItem orderItem3 = OrderItem
                .createOrderItem(savedItem3, 1003, 10, null);

        OrderItem orderItem4 = OrderItem
                .createOrderItem(savedItem3, 1003, 10, null);

        OrderItem orderItem5 = OrderItem
                .createOrderItem(savedItem3, 1003, 10, null);

        // delivery create
        Delivery delivery1 = createDelivery();
        Delivery savedDelivery1 = deliveryRepository.save(delivery1);

        Delivery delivery2 = createDelivery();
        Delivery savedDelivery2 = deliveryRepository.save(delivery2);

        Delivery delivery3 = createDelivery();
        Delivery savedDelivery3 = deliveryRepository.save(delivery3);

        //orderItem create
        ArrayList<OrderItem> orderItems1 = new ArrayList<>();
        orderItems1.add(orderItem);
        orderItems1.add(orderItem2);

        ArrayList<OrderItem> orderItems2 = new ArrayList<>();
        orderItems2.add(orderItem3);

        ArrayList<OrderItem> orderItems3 = new ArrayList<>();
        orderItems3.add(orderItem4);
        orderItems3.add(orderItem5);
        // payment create
        SafekingPayment payment1 = SafekingPayment.createPayment(orderItems1, "123");
        paymentRepository.save(payment1);

        SafekingPayment payment2 = SafekingPayment.createPayment(orderItems2, "1234");
        paymentRepository.save(payment2);

        SafekingPayment payment3 = SafekingPayment.createPayment(orderItems3, "12345");
        paymentRepository.save(payment3);

        //order create
        Order order1 = createOrder(savedMember, savedDelivery1, orderItems1, payment1);

        Order order2 = createOrder(savedMember, savedDelivery2, orderItems2, payment2);

        Order order3 = createOrder(savedMember2, savedDelivery3, orderItems3, payment3);

        Order savedOrder1 = orderRepository.save(order1);
        Order savedOrder2 = orderRepository.save(order2);
        Order savedOrder3 = orderRepository.save(order3);

        // when
        orderService.deleteByMemberBatch(member);

        em.flush(); // 영속성 컨텍스트 내용을 DB에 반영
        em.clear(); // 영속성 컨텍스트 비움
        //then
        assertAll(
                ()->assertThrows(NoSuchElementException.class
                        , () -> orderItemRepository
                                .findById(orderItem.getId())
                                .orElseThrow(NoSuchElementException::new))

                , ()->assertThrows(NoSuchElementException.class
                        , () -> orderItemRepository
                                .findById(orderItem2.getId())
                                .orElseThrow())

                , ()->assertThrows(NoSuchElementException.class
                        , () -> orderItemRepository
                                .findById(orderItem3.getId())
                                .orElseThrow())

                , ()->assertThrows(NoSuchElementException.class
                        , () -> orderRepository
                                .findById(savedOrder1.getId())
                                .orElseThrow())

                , ()->assertThrows(NoSuchElementException.class
                        , () -> orderRepository
                                .findById(savedOrder2.getId())
                                .orElseThrow())

                , ()->assertThrows(NoSuchElementException.class
                        , () -> deliveryRepository
                                .findById(savedDelivery1.getId())
                                .orElseThrow())

                , ()->assertThrows(NoSuchElementException.class
                        , () -> deliveryRepository
                                .findById(savedDelivery2.getId())
                                .orElseThrow())

                , ()->assertThrows(NoSuchElementException.class
                        , () -> paymentRepository
                                .findById(payment1.getId())
                                .orElseThrow())

                , ()->assertThrows(NoSuchElementException.class
                        , () -> paymentRepository
                                .findById(payment2.getId())
                                .orElseThrow())

                , () -> assertThat(deliveryRepository.findAll().size())
                        .isEqualTo(1)

                , () -> assertThat(paymentRepository.findAll().size())
                        .isEqualTo(1)
        );
    }

    @NotNull
    private static Order createOrder(GeneralMember savedMember, Delivery savedDelivery1, ArrayList<OrderItem> orderItems1, SafekingPayment payment) {
        return Order.createOrder(
                savedMember
                , savedDelivery1
                , "memo"
                , payment.getMerchantUid()
                , payment
                , orderItems1);
    }

    @NotNull
    private static Delivery createDelivery() {
        return Delivery.createDelivery(
                "receiver"
                , "phoneNumber"
                , "address"
                , DeliveryStatus.COMPLETE
                , "memo"
                ,"dlwlrma@kakao.com");
    }

    @NotNull
    private static Item getItem() {
        return Item.createItem("name"
                , 100
                , "description"
                , 1000, "adminId"
                , null
                , 100
                , "Y");
    }


}
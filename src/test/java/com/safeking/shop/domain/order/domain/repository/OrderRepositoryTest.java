package com.safeking.shop.domain.order.domain.repository;

import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.item.domain.repository.ItemRepository;
import com.safeking.shop.domain.order.domain.entity.Delivery;
import com.safeking.shop.domain.order.domain.entity.Order;
import com.safeking.shop.domain.order.domain.entity.OrderItem;
import com.safeking.shop.domain.order.domain.entity.status.DeliveryStatus;
import com.safeking.shop.domain.order.domain.service.OrderItemService;
import com.safeking.shop.domain.payment.domain.entity.SafekingPayment;
import com.safeking.shop.domain.payment.domain.repository.SafekingPaymentRepository;
import com.safeking.shop.domain.user.domain.entity.member.GeneralMember;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderRepositoryTest {
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
    OrderItemService orderItemService;
    @Test
    void delete(){
        //given
        GeneralMember member = GeneralMember.builder().build();
        GeneralMember savedMember = memberRepository.save(member);

        Item savedItem1 = itemRepository.save(
                Item.createItem("name"
                        ,10
                        , "description"
                        , 1000, "adminId"
                        , null
                        , 100
                        , "Y"));
        Item savedItem2 = itemRepository.save(
                Item.createItem("name"
                        ,10
                        , "description"
                        , 1000, "adminId"
                        , null
                        , 100
                        , "Y"));
        Item savedItem3 = itemRepository.save(
                Item.createItem("name"
                        ,10
                        , "description"
                        , 1000, "adminId"
                        , null
                        , 100
                        , "Y"));

        OrderItem orderItem = OrderItem
                .createOrderItem(savedItem1, 1000, 10);

        OrderItem orderItem2 = OrderItem
                .createOrderItem(savedItem2, 1000, 10);

        OrderItem orderItem3 = OrderItem
                .createOrderItem(savedItem3, 1000, 10);

        OrderItem savedOrderItem1 = orderItemRepository.save(orderItem);
        OrderItem savedOrderItem2 = orderItemRepository.save(orderItem2);
        OrderItem savedOrderItem3 = orderItemRepository.save(orderItem3);

        Delivery delivery = Delivery.createDelivery(
                "receiver"
                , "phoneNumber"
                , "address"
                , DeliveryStatus.COMPLETE
                , "memo");
        Delivery savedDelivery = deliveryRepository.save(delivery);

        ArrayList<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(savedOrderItem1);
        orderItems.add(savedOrderItem2);
        orderItems.add(savedOrderItem3);

        SafekingPayment payment = SafekingPayment.createPayment(orderItems);
        SafekingPayment savedPayment = paymentRepository.save(payment);

        Order order = Order.createOrder(
                member
                , savedDelivery
                , "memo"
                , savedPayment
                , orderItems);
        Order savedOrder = orderRepository.save(order);

        //when
        orderRepository.delete(savedOrder);
        //then

        assertAll(
                ()->assertThrows(NoSuchElementException.class
                        , () -> orderItemRepository
                                .findById(savedOrderItem1.getId())
                                .orElseThrow())

                , ()->assertThrows(NoSuchElementException.class
                        , () -> orderItemRepository
                            .findById(savedOrderItem2.getId())
                            .orElseThrow())

                , ()->assertThrows(NoSuchElementException.class
                        , () -> orderItemRepository
                                .findById(savedOrderItem3.getId())
                                .orElseThrow())
        );


    }
    @Test
    public void deleteAllByMemberBatch(){
        //given
        GeneralMember member = GeneralMember.builder().build();
        GeneralMember savedMember = memberRepository.save(member);

        Item savedItem1 = itemRepository.save(
                Item.createItem("name"
                        ,10
                        , "description"
                        , 1000, "adminId"
                        , null
                        , 100
                        , "Y"));
        Item savedItem2 = itemRepository.save(
                Item.createItem("name"
                        ,10
                        , "description"
                        , 1000, "adminId"
                        , null
                        , 100
                        , "Y"));
        Item savedItem3 = itemRepository.save(
                Item.createItem("name"
                        ,10
                        , "description"
                        , 1000, "adminId"
                        , null
                        , 100
                        , "Y"));

        OrderItem orderItem = OrderItem
                .createOrderItem(savedItem1, 1000, 10);

        OrderItem orderItem2 = OrderItem
                .createOrderItem(savedItem2, 1000, 10);

        OrderItem orderItem3 = OrderItem
                .createOrderItem(savedItem3, 1000, 10);

//        OrderItem savedOrderItem1 = orderItemRepository.save(orderItem);
//        OrderItem savedOrderItem2 = orderItemRepository.save(orderItem2);
//        OrderItem savedOrderItem3 = orderItemRepository.save(orderItem3);

        Delivery delivery = Delivery.createDelivery(
                "receiver"
                , "phoneNumber"
                , "address"
                , DeliveryStatus.COMPLETE
                , "memo");
        Delivery savedDelivery = deliveryRepository.save(delivery);

        ArrayList<OrderItem> orderItems1 = new ArrayList<>();
        orderItems1.add(orderItem);
        orderItems1.add(orderItem2);

        ArrayList<OrderItem> orderItems2 = new ArrayList<>();
        orderItems2.add(orderItem3);

//        SafekingPayment payment = SafekingPayment.createPayment(orderItems);
//        SafekingPayment savedPayment = paymentRepository.save(payment);

        Order order1 = Order.createOrder(
                savedMember
                , savedDelivery
                , "memo"
                , null
                , orderItems1);
        Order order2 = Order.createOrder(
                savedMember
                , savedDelivery
                , "memo"
                , null
                , orderItems2);

        Order savedOrder1 = orderRepository.save(order1);
        Order savedOrder2 = orderRepository.save(order2);

        // when
        List<Long> orderIds = orderRepository
                .findByMember(savedMember)
                .stream()
                .map(Order::getId)
                .collect(Collectors.toList());

        orderRepository.findByMember(member).stream().forEach(order -> orderItemService.delete(order));

        orderRepository.deleteAllByMemberBatch(orderIds);

        int size = orderRepository.findAll().size();
        System.out.println("size = " + size);

        int size1 = orderItemRepository.findAll().size();
        System.out.println("size1 = " + size1);

        //then
        assertAll(
                ()->assertThrows(NoSuchElementException.class
                        , () -> orderItemRepository
                                .findById(orderItem.getId())
                                .orElseThrow())

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
        );
    }
}
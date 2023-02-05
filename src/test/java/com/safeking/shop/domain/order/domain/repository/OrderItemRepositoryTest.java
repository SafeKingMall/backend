package com.safeking.shop.domain.order.domain.repository;

import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.item.domain.repository.ItemRepository;
import com.safeking.shop.domain.order.domain.entity.Delivery;
import com.safeking.shop.domain.order.domain.entity.Order;
import com.safeking.shop.domain.order.domain.entity.OrderItem;
import com.safeking.shop.domain.order.domain.entity.status.DeliveryStatus;
import com.safeking.shop.domain.order.domain.service.OrderItemService;
import com.safeking.shop.domain.payment.domain.repository.SafekingPaymentRepository;
import com.safeking.shop.domain.user.domain.entity.member.GeneralMember;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class OrderItemRepositoryTest {
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
    @Autowired
    EntityManager em;
    @Test
    void findByOrderList() {
        //given
        GeneralMember member = GeneralMember.builder().build();
        GeneralMember savedMember = memberRepository.save(member);

        GeneralMember member2 = GeneralMember.builder().build();
        GeneralMember savedMember2 = memberRepository.save(member2);

        Item savedItem1 = itemRepository.save(
                Item.createItem("name"
                        ,100
                        , "description"
                        , 1000, "adminId"
                        , null
                        , 100
                        , "Y"));
        Item savedItem2 = itemRepository.save(
                Item.createItem("name"
                        ,100
                        , "description"
                        , 1000, "adminId"
                        , null
                        , 100
                        , "Y"));
        Item savedItem3 = itemRepository.save(
                Item.createItem("name"
                        ,100
                        , "description"
                        , 1000, "adminId"
                        , null
                        , 100
                        , "Y"));

        OrderItem orderItem = OrderItem
                .createOrderItem(savedItem1, 1001, 10, null);

        OrderItem orderItem2 = OrderItem
                .createOrderItem(savedItem2, 1002, 10, null);

        OrderItem orderItem3 = OrderItem
                .createOrderItem(savedItem3, 1003, 10, null);

        OrderItem orderItem4 = OrderItem
                .createOrderItem(savedItem3, 1004, 10, null);

        OrderItem orderItem5 = OrderItem
                .createOrderItem(savedItem3, 1005, 10, null);


        Delivery delivery = Delivery.createDelivery(
                "receiver"
                , "phoneNumber"
                , "address"
                , DeliveryStatus.COMPLETE
                , "memo"
                ,"dlwlrma@kakao.com");
        Delivery savedDelivery = deliveryRepository.save(delivery);

        ArrayList<OrderItem> orderItems1 = new ArrayList<>();
        orderItems1.add(orderItem);
        orderItems1.add(orderItem2);

        ArrayList<OrderItem> orderItems2 = new ArrayList<>();
        orderItems2.add(orderItem3);

        ArrayList<OrderItem> orderItems3 = new ArrayList<>();
        orderItems3.add(orderItem4);
        orderItems3.add(orderItem5);

        Order order1 = Order.createOrder(
                savedMember
                , savedDelivery
                , "memo"
                , "1"
                , null
                , orderItems1);

        Order order2 = Order.createOrder(
                savedMember
                , savedDelivery
                , "memo"
                ,"2"
                , null
                , orderItems2);

        Order order3 = Order.createOrder(
                savedMember2
                , savedDelivery
                , "memo"
                , "3"
                , null
                , orderItems3);

        Order savedOrder1 = orderRepository.save(order1);
        Order savedOrder2 = orderRepository.save(order2);
        Order savedOrder3 = orderRepository.save(order3);

        //when
        List<Order> orderList = orderRepository.findByMember(member);

        List<OrderItem> orderItemList = orderItemRepository.findByOrderList(orderList);

        //then
        assertThat(orderItemList.size()).isEqualTo(3);
    }
}
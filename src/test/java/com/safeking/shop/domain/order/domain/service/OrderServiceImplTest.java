package com.safeking.shop.domain.order.domain.service;

import com.safeking.shop.domain.admin.domain.entity.Admin;
import com.safeking.shop.domain.admin.domain.repository.AdminRepository;
import com.safeking.shop.domain.exception.OrderException;
import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.item.domain.repository.ItemRepository;
import com.safeking.shop.domain.order.domain.entity.Delivery;
import com.safeking.shop.domain.order.domain.entity.Order;
import com.safeking.shop.domain.order.domain.entity.OrderItem;
import com.safeking.shop.domain.order.domain.entity.status.DeliveryStatus;
import com.safeking.shop.domain.order.domain.entity.status.OrderStatus;
import com.safeking.shop.domain.order.domain.repository.DeliveryRepository;
import com.safeking.shop.domain.order.domain.repository.OrderRepository;
import com.safeking.shop.domain.order.web.dto.request.cancel.CancelRequest;
import com.safeking.shop.domain.order.web.dto.request.cancel.CancelOrderRequest;
import com.safeking.shop.domain.order.web.dto.request.order.OrderDeliveryRequest;
import com.safeking.shop.domain.order.web.dto.request.order.OrderRequest;
import com.safeking.shop.domain.order.web.dto.request.order.OrderItemRequest;
import com.safeking.shop.domain.order.web.dto.request.modify.ModifyInfoDeliveryRequest;
import com.safeking.shop.domain.order.web.dto.request.modify.ModifyInfoRequest;
import com.safeking.shop.domain.order.web.dto.request.modify.ModifyInfoOrderRequest;
import com.safeking.shop.domain.user.domain.entity.member.GeneralMember;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class OrderServiceImplTest {
    @PersistenceContext
    EntityManager em;
    @Autowired
    OrderServiceImpl orderService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    DeliveryRepository deliveryRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    AdminRepository adminRepository;

    @Test
    @Transactional
    void 주문() throws Exception {
        //given
        Member generalMember = GeneralMember.builder()
                .name("아이유")
                .username("dlwlrma")
                .password("1234")
                .build();

        memberRepository.save(generalMember);

        Item item = Item.createItem("안전모",
                100,
                "안전모 입니다.",
                1000,
                new Admin("admin", "1"));

        itemRepository.save(item);

        OrderDeliveryRequest orderDeliveryRequest = new OrderDeliveryRequest();
        orderDeliveryRequest.setMemo("안전하게 배송해주세요.");
        OrderItemRequest orderItemRequest = new OrderItemRequest(item.getId(), 2);
        OrderRequest orderRequest = new OrderRequest(generalMember.getName(),
                generalMember.getPhoneNumber(),
                "서울시 강남구",
                "납기일 준수 해주세요.",
                List.of(orderItemRequest),
                orderDeliveryRequest);

        //when
        Long orderId = orderService.order(generalMember, orderRequest);
        Optional<Order> findOrder = orderRepository.findById(orderId);

        //then
        assertThat(findOrder.get().getMember().getName()).isEqualTo("아이유");
    }

    @Test
    @Transactional
    void 주문_취소() throws Exception {

        //given
        Delivery delivery = Delivery.createDelivery("아이유",
                "01012341234",
                "서울시 여의도",
                DeliveryStatus.PREPARATION,
                "문앞에 놓아주세요.");
        Item item = Item.createItem("안전모",
                1,
                "안전모 입니다.",
                3000,
                new Admin("dlwlrma", "1234"));
        OrderItem orderItem = OrderItem.createOrderItem(item, 3000, 1);

        Member generalMember = GeneralMember.builder()
                .name("아이유")
                .username("dlwlrma")
                .password("1234")
                .build();

        Order order = Order.createOrder(generalMember, delivery, "납기일 준수해주세요.", List.of(orderItem));
        orderRepository.save(order);

        CancelRequest cancelRequest = new CancelRequest();
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest();
        cancelOrderRequest.setId(order.getId());
        cancelRequest.setOrders(List.of(cancelOrderRequest));

        //when
        orderService.cancel(cancelRequest);
        Optional<Order> findOrder = orderRepository.findById(order.getId());

        //then
        assertThat(findOrder.get().getStatus()).isEqualTo(OrderStatus.CANCEL);
    }

    @Test
    @Transactional
    void 주문_취소_배송중() throws Exception {
        //given
        OrderDeliveryRequest orderDeliveryRequest = new OrderDeliveryRequest();
        orderDeliveryRequest.setMemo("문앞에 놓아주세요.");

        OrderRequest orderRequest = new OrderRequest("아이유",
                "01012341234",
                "서울시 여의도",
                "납기일 준수해주세요.",
                List.of(new OrderItemRequest(1L, 1000)),
                orderDeliveryRequest);

        //배송 중
        Delivery delivery = Delivery.createDelivery(
                orderRequest.getReceiver(),
                orderRequest.getPhoneNumber(),
                orderRequest.getAddress(),
                DeliveryStatus.IN_DELIVERY,
                orderRequest.getOrderDeliveryRequest().getMemo());

        Item item = Item.createItem("안전모",
                1,
                "안전모 입니다.",
                3000,
                new Admin("dlwlrma", "1234"));
        OrderItem orderItem = OrderItem.createOrderItem(item, 3000, 1);

        Member generalMember = GeneralMember.builder()
                .name("아이유")
                .username("dlwlrma")
                .password("1234")
                .build();

        Order order = Order.createOrder(generalMember, delivery, orderRequest.getMemo(), List.of(orderItem));
        orderRepository.save(order);

        CancelRequest cancelRequest = new CancelRequest();
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest();
        cancelOrderRequest.setId(order.getId());
        cancelRequest.setOrders(List.of(cancelOrderRequest));

        //when
        deliveryRepository.save(delivery);

        //then
        assertThrows(OrderException.class, () -> orderService.cancel(cancelRequest));
    }

    @Test
    @Transactional
    void 주문_취소_배송완료() throws Exception {
        //given
        OrderDeliveryRequest orderDeliveryRequest = new OrderDeliveryRequest();
        orderDeliveryRequest.setMemo("문앞에 놓아주세요.");

        OrderRequest orderRequest = new OrderRequest("아이유",
                "01012341234",
                "서울시 여의도",
                "납기일 준수해주세요.",
                List.of(new OrderItemRequest(1L, 1000)),
                orderDeliveryRequest);

        //배송 완료
        Delivery delivery = Delivery.createDelivery(
                orderRequest.getReceiver(),
                orderRequest.getPhoneNumber(),
                orderRequest.getAddress(),
                DeliveryStatus.COMPLETE,
                orderRequest.getOrderDeliveryRequest().getMemo());

        Item item = Item.createItem("안전모",
                1,
                "안전모 입니다.",
                3000,
                new Admin("dlwlrma", "1234"));

        Member generalMember = GeneralMember.builder()
                .name("아이유")
                .username("dlwlrma")
                .password("1234")
                .build();

        OrderItem orderItem = OrderItem.createOrderItem(item, 3000, 1);
        Order order = Order.createOrder(generalMember, delivery, orderRequest.getMemo(), List.of(orderItem));
        orderRepository.save(order);

        CancelRequest cancelRequest = new CancelRequest();
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest();
        cancelOrderRequest.setId(order.getId());
        cancelRequest.setOrders(List.of(cancelOrderRequest));

        //when
        deliveryRepository.save(delivery);

        //then
        assertThrows(OrderException.class, () -> orderService.cancel(cancelRequest));
    }

    @Test
    @Transactional
    void 주문_수정() throws Exception {
        //given
        OrderDeliveryRequest orderDeliveryRequest = new OrderDeliveryRequest();
        orderDeliveryRequest.setMemo("문앞에 놓아주세요.");

        OrderRequest orderRequest = new OrderRequest("아이유",
                "01012341234",
                "서울시 여의도",
                "납기일 준수해주세요.",
                List.of(new OrderItemRequest(1L, 1000)),
                orderDeliveryRequest);

        //배송 완료
        Delivery delivery = Delivery.createDelivery(
                orderRequest.getReceiver(),
                orderRequest.getPhoneNumber(),
                orderRequest.getAddress(),
                DeliveryStatus.PREPARATION,
                orderRequest.getOrderDeliveryRequest().getMemo());

        Member generalMember = GeneralMember.builder()
                .name("아이유")
                .username("dlwlrma")
                .password("1234")
                .build();

        Item item = Item.createItem("안전모",
                1,
                "안전모 입니다.",
                3000,
                new Admin("dlwlrma", "1234"));
        OrderItem orderItem = OrderItem.createOrderItem(item, 3000, 1);
        Order order = Order.createOrder(generalMember, delivery, orderRequest.getMemo(), List.of(orderItem));
        orderRepository.save(order);

        //when
        ModifyInfoRequest modifyInfoRequest = new ModifyInfoRequest();
        ModifyInfoOrderRequest modifyInfoOrderRequest = new ModifyInfoOrderRequest();
        ModifyInfoDeliveryRequest modifyInfoDeliveryRequest = new ModifyInfoDeliveryRequest();
        modifyInfoOrderRequest.setId(order.getId());
        modifyInfoOrderRequest.setMemo("반갑습니다.");

        modifyInfoDeliveryRequest.setReceiver("홍길동");
        modifyInfoDeliveryRequest.setAddress("부산 진구");
        modifyInfoDeliveryRequest.setPhoneNumber("01043214321");
        modifyInfoDeliveryRequest.setMemo("문앞에 놓아주세요.");

        modifyInfoRequest.setOrder(modifyInfoOrderRequest);
        modifyInfoRequest.setDelivery(modifyInfoDeliveryRequest);

        Long modifyOrderId = orderService.modifyOrder(modifyInfoRequest);

        //then
        assertThat(order.getId()).isEqualTo(modifyOrderId);
        assertThat(order.getMemo()).isEqualTo("반갑습니다.");
        assertThat(order.getDelivery().getReceiver()).isEqualTo("홍길동");
        assertThat(order.getDelivery().getAddress()).isEqualTo("부산 진구");
        assertThat(order.getDelivery().getPhoneNumber()).isEqualTo("01043214321");
        assertThat(order.getDelivery().getMemo()).isEqualTo("문앞에 놓아주세요.");
    }


    @Test
    @Transactional
    void 주문_정보_조회() throws Exception {
        //given
        Member generalMember = GeneralMember.builder()
                .name("아이유")
                .username("dlwlrma")
                .password("1234")
                .build();

        memberRepository.save(generalMember);

        Admin admin = new Admin("admin", "1");
        adminRepository.save(admin);

        Item item = Item.createItem("안전모",
                100,
                "안전모 입니다.",
                1000,
                admin);

        itemRepository.save(item);

        OrderDeliveryRequest orderDeliveryRequest = new OrderDeliveryRequest();
        orderDeliveryRequest.setMemo("문앞에 놓아주세요.");

        OrderRequest orderRequest = new OrderRequest("아이유",
                "01012341234",
                "서울시 여의도",
                "납기일 준수해주세요.",
                List.of(new OrderItemRequest(1L, 1000)),
                orderDeliveryRequest);

        //배송 완료
        Delivery delivery = Delivery.createDelivery(
                orderRequest.getReceiver(),
                orderRequest.getPhoneNumber(),
                orderRequest.getAddress(),
                DeliveryStatus.PREPARATION,
                orderRequest.getOrderDeliveryRequest().getMemo());
        deliveryRepository.save(delivery);

        OrderItem orderItem = OrderItem.createOrderItem(item, 3000, 1);
        Order order = Order.createOrder(generalMember, delivery, orderRequest.getMemo(), List.of(orderItem));
        orderRepository.save(order);

        //when
        Order findOrder = orderService.findOrder(order.getId());

        //then
        assertThat(findOrder.getId()).isEqualTo(order.getId());
        assertThat(findOrder.getDelivery().getReceiver()).isEqualTo(delivery.getReceiver());
        assertThat(findOrder.getDelivery().getPhoneNumber()).isEqualTo(delivery.getPhoneNumber());
        assertThat(findOrder.getDelivery().getAddress()).isEqualTo(delivery.getAddress());
        assertThat(findOrder.getDelivery().getMemo()).isEqualTo(delivery.getMemo());


        FindOrderDto findOrderDto = new FindOrderDto(findOrder.getId(),
                findOrder.getMemo(),
                findOrder.getDelivery().getReceiver(),
                findOrder.getDelivery().getPhoneNumber(),
                findOrder.getDelivery().getAddress(),
                findOrder.getDelivery().getMemo());
        System.out.println(findOrderDto);

    }

    @Data
    @AllArgsConstructor
    @ToString(of = {"id", "orderMemo", "receiver", "phoneNumber", "address", "deliveryMemo"})
    static class FindOrderDto {
        private Long id;
        private String orderMemo;
        private String receiver;
        private String phoneNumber;
        private String address;
        private String deliveryMemo;
    }
}
package com.safeking.shop.domain.order.domain.service;

import com.safeking.shop.domain.admin.domain.entity.Admin;
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
import com.safeking.shop.domain.order.web.dto.request.cancel.CancelDto;
import com.safeking.shop.domain.order.web.dto.request.cancel.CancelOrderDtos;
import com.safeking.shop.domain.order.web.dto.request.order.OrderDeliveryDto;
import com.safeking.shop.domain.order.web.dto.request.order.OrderDto;
import com.safeking.shop.domain.order.web.dto.request.order.OrderItemDto;
import com.safeking.shop.domain.order.web.dto.request.modify.ModifyInfoDeliveryDto;
import com.safeking.shop.domain.order.web.dto.request.modify.ModifyInfoDto;
import com.safeking.shop.domain.order.web.dto.request.modify.ModifyInfoOrderDto;
import com.safeking.shop.domain.user.domain.entity.member.GeneralMember;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class OrderServiceImplTest {
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

        OrderDeliveryDto orderDeliveryDto = new OrderDeliveryDto();
        orderDeliveryDto.setMemo("안전하게 배송해주세요.");
        OrderItemDto orderItemDto = new OrderItemDto(item.getId(), 2);
        OrderDto orderDto = new OrderDto(generalMember.getName(),
                generalMember.getPhoneNumber(),
                "서울시 강남구",
                "납기일 준수 해주세요.",
                List.of(orderItemDto),
                orderDeliveryDto);

        //when
        Long orderId = orderService.order(generalMember, orderDto);
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

        CancelDto cancelDto = new CancelDto();
        CancelOrderDtos cancelOrderDtos = new CancelOrderDtos();
        cancelOrderDtos.setId(order.getId());
        cancelDto.setOrders(List.of(cancelOrderDtos));

        //when
        orderService.cancel(cancelDto);
        Optional<Order> findOrder = orderRepository.findById(order.getId());

        //then
        assertThat(findOrder.get().getStatus()).isEqualTo(OrderStatus.CANCEL);
    }

    @Test
    @Transactional
    void 주문_취소_배송중() throws Exception {
        //given
        OrderDeliveryDto orderDeliveryDto = new OrderDeliveryDto();
        orderDeliveryDto.setMemo("문앞에 놓아주세요.");

        OrderDto orderDto = new OrderDto("아이유",
                "01012341234",
                "서울시 여의도",
                "납기일 준수해주세요.",
                List.of(new OrderItemDto(1L, 1000)),
                orderDeliveryDto);

        //배송 중
        Delivery delivery = Delivery.createDelivery(
                orderDto.getReceiver(),
                orderDto.getPhoneNumber(),
                orderDto.getAddress(),
                DeliveryStatus.IN_DELIVERY,
                orderDto.getOrderDeliveryDto().getMemo());

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

        Order order = Order.createOrder(generalMember, delivery, orderDto.getMemo(), List.of(orderItem));
        orderRepository.save(order);

        CancelDto cancelDto = new CancelDto();
        CancelOrderDtos cancelOrderDtos = new CancelOrderDtos();
        cancelOrderDtos.setId(order.getId());
        cancelDto.setOrders(List.of(cancelOrderDtos));

        //when
        deliveryRepository.save(delivery);

        //then
        assertThrows(OrderException.class, () -> orderService.cancel(cancelDto));
    }

    @Test
    @Transactional
    void 주문_취소_배송완료() throws Exception {
        //given
        OrderDeliveryDto orderDeliveryDto = new OrderDeliveryDto();
        orderDeliveryDto.setMemo("문앞에 놓아주세요.");

        OrderDto orderDto = new OrderDto("아이유",
                "01012341234",
                "서울시 여의도",
                "납기일 준수해주세요.",
                List.of(new OrderItemDto(1L, 1000)),
                orderDeliveryDto);

        //배송 완료
        Delivery delivery = Delivery.createDelivery(
                orderDto.getReceiver(),
                orderDto.getPhoneNumber(),
                orderDto.getAddress(),
                DeliveryStatus.COMPLETE,
                orderDto.getOrderDeliveryDto().getMemo());

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
        Order order = Order.createOrder(generalMember, delivery, orderDto.getMemo(), List.of(orderItem));
        orderRepository.save(order);

        CancelDto cancelDto = new CancelDto();
        CancelOrderDtos cancelOrderDtos = new CancelOrderDtos();
        cancelOrderDtos.setId(order.getId());
        cancelDto.setOrders(List.of(cancelOrderDtos));

        //when
        deliveryRepository.save(delivery);

        //then
        assertThrows(OrderException.class, () -> orderService.cancel(cancelDto));
    }

    @Test
    @Transactional
    void 주문_수정() throws Exception {
        //given
        OrderDeliveryDto orderDeliveryDto = new OrderDeliveryDto();
        orderDeliveryDto.setMemo("문앞에 놓아주세요.");

        OrderDto orderDto = new OrderDto("아이유",
                "01012341234",
                "서울시 여의도",
                "납기일 준수해주세요.",
                List.of(new OrderItemDto(1L, 1000)),
                orderDeliveryDto);

        //배송 완료
        Delivery delivery = Delivery.createDelivery(
                orderDto.getReceiver(),
                orderDto.getPhoneNumber(),
                orderDto.getAddress(),
                DeliveryStatus.PREPARATION,
                orderDto.getOrderDeliveryDto().getMemo());

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
        Order order = Order.createOrder(generalMember, delivery, orderDto.getMemo(), List.of(orderItem));
        orderRepository.save(order);

        //when
        ModifyInfoDto modifyInfoDto = new ModifyInfoDto();
        ModifyInfoOrderDto modifyInfoOrderDto = new ModifyInfoOrderDto();
        ModifyInfoDeliveryDto modifyInfoDeliveryDto = new ModifyInfoDeliveryDto();
        modifyInfoOrderDto.setId(order.getId());
        modifyInfoOrderDto.setMemo("반갑습니다.");

        modifyInfoDeliveryDto.setReceiver("홍길동");
        modifyInfoDeliveryDto.setAddress("부산 진구");
        modifyInfoDeliveryDto.setPhoneNumber("01043214321");
        modifyInfoDeliveryDto.setMemo("문앞에 놓아주세요.");

        modifyInfoDto.setOrder(modifyInfoOrderDto);
        modifyInfoDto.setDelivery(modifyInfoDeliveryDto);

        Long modifyOrderId = orderService.modifyOrder(modifyInfoDto);

        //then
        assertThat(order.getId()).isEqualTo(modifyOrderId);
        assertThat(order.getMemo()).isEqualTo("반갑습니다.");
        assertThat(order.getDelivery().getReceiver()).isEqualTo("홍길동");
        assertThat(order.getDelivery().getAddress()).isEqualTo("부산 진구");
        assertThat(order.getDelivery().getPhoneNumber()).isEqualTo("01043214321");
        assertThat(order.getDelivery().getMemo()).isEqualTo("문앞에 놓아주세요.");
    }

}
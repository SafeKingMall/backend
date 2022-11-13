package com.safeking.shop.domain.order.domain.service;

import com.safeking.shop.domain.admin.domain.entity.Admin;
import com.safeking.shop.domain.exception.OrderException;
import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.order.domain.entity.Delivery;
import com.safeking.shop.domain.order.domain.entity.Order;
import com.safeking.shop.domain.order.domain.entity.OrderItem;
import com.safeking.shop.domain.order.domain.entity.status.DeliveryStatus;
import com.safeking.shop.domain.order.domain.entity.status.OrderStatus;
import com.safeking.shop.domain.order.domain.repository.DeliveryRepository;
import com.safeking.shop.domain.order.domain.repository.OrderRepository;
import com.safeking.shop.domain.order.web.dto.request.cancel.CancelDto;
import com.safeking.shop.domain.order.web.dto.request.cancel.CancelOrderDtos;
import com.safeking.shop.domain.order.domain.service.dto.order.OrderDeliveryDto;
import com.safeking.shop.domain.order.domain.service.dto.order.OrderDto;
import com.safeking.shop.domain.order.domain.service.dto.order.OrderItemDto;
import com.safeking.shop.domain.user.domain.entity.Member;
import com.safeking.shop.domain.user.domain.entity.MemberAccountType;
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
        Order order = Order.createOrder(new Member(MemberAccountType.NORMAL), delivery, "납기일 준수해주세요.", List.of(orderItem));
        orderRepository.save(order);

        CancelDto cancelDto = new CancelDto();
        CancelOrderDtos cancelOrderDtos = new CancelOrderDtos();
        cancelOrderDtos.setId(order.getId());
        cancelDto.setCancelOrderDtos(List.of(cancelOrderDtos));

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
        Order order = Order.createOrder(new Member(MemberAccountType.NORMAL), delivery, orderDto.getMemo(), List.of(orderItem));
        orderRepository.save(order);

        CancelDto cancelDto = new CancelDto();
        CancelOrderDtos cancelOrderDtos = new CancelOrderDtos();
        cancelOrderDtos.setId(order.getId());
        cancelDto.setCancelOrderDtos(List.of(cancelOrderDtos));

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
        OrderItem orderItem = OrderItem.createOrderItem(item, 3000, 1);
        Order order = Order.createOrder(new Member(MemberAccountType.NORMAL), delivery, orderDto.getMemo(), List.of(orderItem));
        orderRepository.save(order);

        CancelDto cancelDto = new CancelDto();
        CancelOrderDtos cancelOrderDtos = new CancelOrderDtos();
        cancelOrderDtos.setId(order.getId());
        cancelDto.setCancelOrderDtos(List.of(cancelOrderDtos));

        //when
        deliveryRepository.save(delivery);

        //then
        assertThrows(OrderException.class, () -> orderService.cancel(cancelDto));
    }
}
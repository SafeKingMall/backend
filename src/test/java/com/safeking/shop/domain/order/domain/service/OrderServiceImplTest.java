package com.safeking.shop.domain.order.domain.service;

import com.safeking.shop.domain.admin.domain.entity.Admin;
import com.safeking.shop.domain.admin.domain.repository.AdminRepository;
import com.safeking.shop.domain.exception.OrderException;
import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.item.domain.repository.ItemRepository;
import com.safeking.shop.domain.order.domain.entity.Delivery;
import com.safeking.shop.domain.order.domain.entity.Order;
import com.safeking.shop.domain.order.domain.entity.OrderItem;
import com.safeking.shop.domain.order.domain.entity.Payment;
import com.safeking.shop.domain.order.domain.entity.status.DeliveryStatus;
import com.safeking.shop.domain.order.domain.entity.status.OrderStatus;
import com.safeking.shop.domain.order.domain.repository.DeliveryRepository;
import com.safeking.shop.domain.order.domain.repository.OrderRepository;
import com.safeking.shop.domain.order.domain.repository.PaymentRepository;
import com.safeking.shop.domain.order.web.dto.request.user.cancel.CancelRequest;
import com.safeking.shop.domain.order.web.dto.request.user.cancel.CancelOrderRequest;
import com.safeking.shop.domain.order.web.dto.request.user.order.OrderDeliveryRequest;
import com.safeking.shop.domain.order.web.dto.request.user.order.OrderRequest;
import com.safeking.shop.domain.order.web.dto.request.user.order.OrderItemRequest;
import com.safeking.shop.domain.order.web.dto.request.user.modify.ModifyInfoDeliveryRequest;
import com.safeking.shop.domain.order.web.dto.request.user.modify.ModifyInfoRequest;
import com.safeking.shop.domain.order.web.dto.request.user.modify.ModifyInfoOrderRequest;
import com.safeking.shop.domain.order.web.dto.request.user.search.OrderSearchCondition;
import com.safeking.shop.domain.user.domain.entity.Address;
import com.safeking.shop.domain.user.domain.entity.member.GeneralMember;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
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
    @Autowired
    PaymentRepository paymentRepository;

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
        Order findOrder = orderService.searchOrder(order.getId());

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

    @Test
    @Transactional
    void 주문_상세_조회() throws Exception {

        em.flush();
        em.clear();

        //given
        Member generalMember = GeneralMember.builder()
                .name("아이유")
                .username("dlwlrma")
                .password("1234")
                .build();
        memberRepository.save(generalMember);

        Admin admin = new Admin("admin", "1");
        adminRepository.save(admin);

        Item item1 = Item.createItem("안전모",
                100,
                "안전모 입니다.",
                1000,
                admin);
        Item item2 = Item.createItem("안전화",
                200,
                "안전화 입니다.",
                2000,
                admin);
        itemRepository.save(item1);
        itemRepository.save(item2);
        OrderItem orderItem1 = OrderItem.createOrderItem(item1, 1000, 1);
        OrderItem orderItem2 = OrderItem.createOrderItem(item2, 2000, 1);

        OrderDeliveryRequest orderDeliveryRequest = new OrderDeliveryRequest();
        orderDeliveryRequest.setMemo("문앞에 놓아주세요.");

        OrderRequest orderRequest = new OrderRequest("아이유",
                "01012341234",
                "서울시 여의도",
                "납기일 준수해주세요.",
                List.of(new OrderItemRequest(1L, 1), new OrderItemRequest(2L, 1 )),
                orderDeliveryRequest);

        //배송 완료
        Delivery delivery = Delivery.createDelivery(
                orderRequest.getReceiver(),
                orderRequest.getPhoneNumber(),
                orderRequest.getAddress(),
                DeliveryStatus.PREPARATION,
                orderRequest.getOrderDeliveryRequest().getMemo());
        deliveryRepository.save(delivery);

        Payment payment = Payment.createPayment(List.of(orderItem1, orderItem2), "123412341234", "카드");
        paymentRepository.save(payment);

        Order order = Order.createOrder(generalMember, delivery, orderRequest.getMemo(), List.of(orderItem1, orderItem2));
        Order orderSave = orderRepository.save(order);
        orderSave.changePayment(payment);

        //when
        Order orderDetail = orderService.searchOrderDetail(orderSave.getId());

        //then
        assertThat(orderDetail.getId()).isEqualTo(order.getId());
        assertThat(orderDetail.getStatus()).isEqualTo(order.getStatus());
        assertThat(orderDetail.getPayment().getPrice()).isEqualTo(payment.getPrice());
        assertThat(orderDetail.getMemo()).isEqualTo(order.getMemo());
        assertThat(orderDetail.getCreateDate()).isEqualTo(order.getCreateDate());
        assertThat(orderDetail.getOrderItems()).isSameAs(order.getOrderItems());
        assertThat(orderDetail.getPayment().getStatus()).isEqualTo(order.getPayment().getStatus());
        assertThat(orderDetail.getDelivery().getId()).isEqualTo(order.getDelivery().getId());
        assertThat(orderDetail.getDelivery().getStatus()).isEqualTo(order.getDelivery().getStatus());
        assertThat(orderDetail.getDelivery().getReceiver()).isEqualTo(order.getDelivery().getReceiver());
        assertThat(orderDetail.getDelivery().getPhoneNumber()).isEqualTo(order.getDelivery().getPhoneNumber());
        assertThat(orderDetail.getDelivery().getAddress()).isEqualTo(order.getDelivery().getAddress());
        assertThat(orderDetail.getDelivery().getMemo()).isEqualTo(order.getDelivery().getMemo());
    }

    @Test
    @Transactional
    @Commit
    void 주문_다건_조회() throws Exception {

        em.flush();
        em.clear();

        //given
        Member generalMember = GeneralMember.builder()
                .name("kim")
                .username("abc12344")
                .password("$2a$10$x2bbVjn7zz8C18sI9xCnJuDqkRbVIYQRqG.LVNNGbiEM20Rz.DYhe")
                .email("abc@gamil.com")
                .phoneNumber("01011111111")
                .address(new Address("서울시", "마포대로", "303동 301호"))
                .roles("ROLE_USER")
                .build();
        Member saveMember = memberRepository.save(generalMember);

        Admin admin = new Admin("admin", "1");
        adminRepository.save(admin);

        Item item1 = Item.createItem("안전모",
                100,
                "안전모 입니다.",
                1000,
                admin);
        Item item2 = Item.createItem("안전화",
                200,
                "안전화 입니다.",
                2000,
                admin);
        itemRepository.save(item1);
        itemRepository.save(item2);
        OrderItem orderItem1 = OrderItem.createOrderItem(item1, 1000, 1);
        OrderItem orderItem2 = OrderItem.createOrderItem(item2, 2000, 1);

        OrderDeliveryRequest orderDeliveryRequest = new OrderDeliveryRequest();
        orderDeliveryRequest.setMemo("문앞에 놓아주세요.");

        OrderRequest orderRequest = new OrderRequest("아이유",
                "01012341234",
                "서울시 여의도",
                "납기일 준수해주세요.",
                List.of(new OrderItemRequest(1L, 1), new OrderItemRequest(2L, 1 )),
                orderDeliveryRequest);

        //배송 완료
        Delivery delivery = Delivery.createDelivery(
                orderRequest.getReceiver(),
                orderRequest.getPhoneNumber(),
                orderRequest.getAddress(),
                DeliveryStatus.PREPARATION,
                orderRequest.getOrderDeliveryRequest().getMemo());
        deliveryRepository.save(delivery);

        Payment payment = Payment.createPayment(List.of(orderItem1, orderItem2), "123412341234", "카드");
        paymentRepository.save(payment);

        Order order = Order.createOrder(generalMember, delivery, orderRequest.getMemo(), List.of(orderItem1, orderItem2));
        Order orderSave = orderRepository.save(order);
        orderSave.changePayment(payment);

        //when
        Pageable pageable = PageRequest.of(0, 5);
        OrderSearchCondition condition = OrderSearchCondition.builder()
                .fromDate(LocalDate.now().toString())
                .toDate(LocalDate.now().plusDays(1).toString())
                .build();

        Page<Order> orders = orderService.searchOrders(pageable, condition, saveMember.getId());

        //then
        assertThat(orders.getContent().get(0).getMember()).isSameAs(generalMember);
        assertThat(orders.getContent().get(0).getOrderItems().get(0).getItem().getName()).isEqualTo("안전모");
        assertThat(orders.getContent().get(0).getOrderItems().get(1).getItem().getName()).isEqualTo("안전화");
        assertThat(orders.getContent().get(0)).isSameAs(order);
        assertThat(orders.getContent().get(0).getPayment()).isSameAs(payment);

    }
}
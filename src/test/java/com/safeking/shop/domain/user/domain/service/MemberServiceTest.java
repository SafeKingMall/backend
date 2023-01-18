package com.safeking.shop.domain.user.domain.service;

import com.safeking.shop.domain.cart.domain.service.CartItemService;
import com.safeking.shop.domain.cart.domain.service.CartService;
import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.item.domain.entity.ItemAnswer;
import com.safeking.shop.domain.item.domain.entity.ItemQuestion;
import com.safeking.shop.domain.item.domain.repository.ItemAnswerRepository;
import com.safeking.shop.domain.item.domain.repository.ItemQuestionRepository;
import com.safeking.shop.domain.item.domain.repository.ItemRepository;
import com.safeking.shop.domain.order.domain.entity.Delivery;
import com.safeking.shop.domain.order.domain.entity.Order;
import com.safeking.shop.domain.order.domain.entity.OrderItem;
import com.safeking.shop.domain.order.domain.entity.status.DeliveryStatus;
import com.safeking.shop.domain.order.domain.repository.DeliveryRepository;
import com.safeking.shop.domain.order.domain.repository.OrderItemRepository;
import com.safeking.shop.domain.order.domain.repository.OrderRepository;
import com.safeking.shop.domain.order.domain.service.OrderServiceImpl;
import com.safeking.shop.domain.payment.domain.entity.SafekingPayment;
import com.safeking.shop.domain.payment.domain.repository.SafekingPaymentRepository;
import com.safeking.shop.domain.user.domain.entity.Address;
import com.safeking.shop.domain.user.domain.entity.MemberStatus;
import com.safeking.shop.domain.user.domain.entity.RedisMember;
import com.safeking.shop.domain.user.domain.entity.member.GeneralMember;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.repository.MemberRedisRepository;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import com.safeking.shop.domain.user.domain.service.dto.MemberUpdateDto;
import com.safeking.shop.domain.user.web.request.UpdateRequest;
import com.safeking.shop.global.config.CustomBCryPasswordEncoder;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CustomBCryPasswordEncoder encoder;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderItemRepository orderItemRepository;
    @Autowired
    DeliveryRepository deliveryRepository;
    @Autowired
    SafekingPaymentRepository paymentRepository;
    @Autowired
    OrderServiceImpl orderService;
    @Autowired
    EntityManager em;
    @Autowired
    CartService cartService;
    @Autowired
    CartItemService cartItemService;
    @Autowired
    ItemQuestionRepository questionRepository;
    @Autowired
    ItemAnswerRepository answerRepository;
    @Autowired
    MemberRedisRepository redisRepository;

    @BeforeEach
    void init(){
        GeneralMember member = GeneralMember.builder()
                .name("user")
                .birth("birth")
                .username("username")
                .password(encoder.encode("password"))
                .email("email")
                .roles("ROLE_USER")
                .phoneNumber("01012345678")
                .companyName("safeking")
                .companyRegistrationNumber("111")
                .corporateRegistrationNumber("222")
                .representativeName("MS")
                .contact("contact")
                .address(new Address("서울시", "마포대로", "111"))
                .agreement(true)
                .accountNonLocked(true)
                .status(MemberStatus.COMMON)
                .build();
        member.addLastLoginTime();
        memberRepository.save(member);
    }

    @Test
    @DisplayName("1. idDuplicationCheck")
    void idDuplicationCheck() {
        //given
        String username1="username";
        String username2="username2";
        //when
        boolean NO = memberService.idDuplicationCheck(username1);
        boolean YES = memberService.idDuplicationCheck(username2);
        //then
        assertAll(
                ()->assertThat(NO).isEqualTo(false),
                ()->assertThat(YES).isEqualTo(true)
        );

    }

    @Test
    @DisplayName("2. updateMemberInfo")
    void updateMemberInfo() {
        //given
        MemberUpdateDto memberUpdateDto = MemberUpdateDto.builder()
                .name("nameChange")
                .birth("birthChange")
                .representativeName("representativeNameChange")
                .phoneNumber("phoneNumberChange")
                .companyRegistrationNumber("companyRegistrationNumberChange")
                .corporateRegistrationNumber("corporateRegistrationNumberChange")
                .address(new Address("basicAddressChange", "detailedAddressChange", "zipcodeChange"))
                .build();
        //when
        memberService.updateMemberInfo("username",memberUpdateDto);
        //then
        Member member = memberRepository.findByUsername("username").orElseThrow();

        assertAll(
                ()->assertThat(member.getName()).isEqualTo("nameChange")
                ,()->assertThat(member.getBirth()).isEqualTo("birthChange")
                ,()->assertThat(member.getRepresentativeName()).isEqualTo("representativeNameChange")
                ,()->assertThat(member.getPhoneNumber()).isEqualTo("phoneNumberChange")
                ,()->assertThat(member.getCompanyRegistrationNumber()).isEqualTo("companyRegistrationNumberChange")
                ,()->assertThat(member.getCorporateRegistrationNumber()).isEqualTo("corporateRegistrationNumberChange")
                ,()->assertThat(member.getAddress())
                        .usingRecursiveComparison()
                        .isEqualTo(new Address("basicAddressChange", "detailedAddressChange", "zipcodeChange"))
       );
    }

//    @Test
//    @Timeout(value = 20,unit = TimeUnit.SECONDS)
//    @DisplayName("3. humanAccountConverterBatch")
//    void humanAccountConverterBatch() throws InterruptedException {
//        //given
//        for (int i = 1; i <=5 ; i++) {
//            GeneralMember member = GeneralMember.builder()
//                    .name("user"+i)
//                    .birth("birth")
//                    .username("username"+i)
//                    .password(encoder.encode("password"))
//                    .email("email")
//                    .roles("ROLE_USER")
//                    .phoneNumber("01012345678")
//                    .companyName("safeking")
//                    .companyRegistrationNumber("111")
//                    .corporateRegistrationNumber("222")
//                    .representativeName("MS")
//                    .contact("contact")
//                    .address(new Address("서울시", "마포대로", "111"))
//                    .agreement(true)
//                    .accountNonLocked(true)
//                    .status(MemberStatus.COMMON)
//                    .build();
//            member.addLastLoginTime();
//            memberRepository.save(member);
//        }
//        Thread.sleep(1000*Member.MEMBER_HUMAN_TIME);
//
//        //when
//        memberService.humanAccountConverterBatch();
//
//        //then
//        memberRepository.findAll().stream().forEach(member -> {
//             assertThat(member.getAccountNonLocked()).isFalse();
//             assertThat(member.getStatus()).isEqualTo(MemberStatus.HUMAN);
//        });
//    }

    @Test
    public void withDrawl() {
        //given
        // member create
        GeneralMember member = GeneralMember.builder().username("member1").build();
        GeneralMember savedMember = memberRepository.save(member);

        GeneralMember member2 = GeneralMember.builder().username("member2").build();
        GeneralMember savedMember2 = memberRepository.save(member2);

        // test login
        redisRepository.save(new RedisMember("user", member.getUsername()));

        //cart Create
        Long cartId = cartService.createCart(member);
        Long cartId2 = cartService.createCart(member2);

        // item create
        Item savedItem1 = itemRepository.save(getItem());
        Item savedItem2 = itemRepository.save(getItem());
        Item savedItem3 = itemRepository.save(getItem());

        OrderItem orderItem = OrderItem
                .createOrderItem(savedItem1, 1001, 10);

        OrderItem orderItem2 = OrderItem
                .createOrderItem(savedItem2, 1002, 10);

        OrderItem orderItem3 = OrderItem
                .createOrderItem(savedItem3, 1003, 10);

        OrderItem orderItem4 = OrderItem
                .createOrderItem(savedItem3, 1003, 10);

        OrderItem orderItem5 = OrderItem
                .createOrderItem(savedItem3, 1003, 10);

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
        SafekingPayment payment1 = SafekingPayment.createPayment(orderItems1);
        paymentRepository.save(payment1);

        SafekingPayment payment2 = SafekingPayment.createPayment(orderItems2);
        paymentRepository.save(payment2);

        SafekingPayment payment3 = SafekingPayment.createPayment(orderItems3);
        paymentRepository.save(payment3);

        //order create
        Order order1 = createOrder(savedMember, savedDelivery1, orderItems1, payment1);

        Order order2 = createOrder(savedMember, savedDelivery2, orderItems2, payment2);

        Order order3 = createOrder(savedMember2, savedDelivery3, orderItems3, payment3);

        Order savedOrder1 = orderRepository.save(order1);
        Order savedOrder2 = orderRepository.save(order2);
        Order savedOrder3 = orderRepository.save(order3);

        // cartItem create
        cartItemService.putCart("member1", savedItem1.getId(), 3);
        cartItemService.putCart("member1", savedItem2.getId(), 3);
        cartItemService.putCart("member2", savedItem1.getId(), 3);
        cartItemService.putCart("member2", savedItem2.getId(), 3);

        // itemQuestion create
        ItemQuestion itemQuestion1 = ItemQuestion.createItemQuestion("title1", "content1", savedItem1, member);
        ItemQuestion itemQuestion2 = ItemQuestion.createItemQuestion("title1", "content1", savedItem1, member);
        ItemQuestion itemQuestion3 = ItemQuestion.createItemQuestion("title1", "content1", savedItem1, member2);

        questionRepository.save(itemQuestion1);
        questionRepository.save(itemQuestion2);
        questionRepository.save(itemQuestion3);

        // itemAnswer create
        ItemAnswer.createItemAnswer(member, itemQuestion1, "content1");
        ItemAnswer.createItemAnswer(member, itemQuestion2, "content2");
        ItemAnswer.createItemAnswer(member, itemQuestion3, "content3");

        ItemAnswer.createItemAnswer(member2, itemQuestion1, "content4");
        ItemAnswer.createItemAnswer(member2, itemQuestion2, "content5");
        ItemAnswer.createItemAnswer(member2, itemQuestion3, "content6");

        //when
        memberService.withdrawal("member1");

        em.flush(); // 영속성 컨텍스트 내용을 DB에 반영
        em.clear(); // 영속성 컨텍스트 비움


    }
    @Test
    public void changeToWithDrawlStatus(){
        //given
        Member member = memberRepository.findByUsername("username").orElseThrow();
        //when
        member.changeToWithDrawlStatus();

        em.flush();
        em.clear();

        //then
        Member findMember = memberRepository.findByUsername("username").orElseThrow();

        assertThat(findMember.getStatus()).isEqualTo(MemberStatus.WITHDRAWAL);
    }

    @NotNull
    private static Order createOrder(GeneralMember savedMember, Delivery savedDelivery1, ArrayList<OrderItem> orderItems1, SafekingPayment payment) {
        return Order.createOrder(
                savedMember
                , savedDelivery1
                , "memo"
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
                , "memo");
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
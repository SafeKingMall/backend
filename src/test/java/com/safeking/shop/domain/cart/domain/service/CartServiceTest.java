//package com.safeking.shop.domain.cart.domain.service;
//
//import com.safeking.shop.domain.cart.domain.entity.Cart;
//import com.safeking.shop.domain.cart.domain.repository.CartRepository;
//import com.safeking.shop.domain.item.domain.entity.Item;
//import com.safeking.shop.domain.item.domain.repository.ItemRepository;
//import com.safeking.shop.domain.user.domain.entity.member.GeneralMember;
//import com.safeking.shop.domain.user.domain.repository.MemberRepository;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Commit;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.NoSuchElementException;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.*;
//@SpringBootTest
//@ActiveProfiles("test")
//@Transactional
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//class CartServiceTest {
//
//    @Autowired
//    CartService cartService;
//    @Autowired
//    MemberRepository memberRepository;
//    @Autowired
//    CartRepository cartRepository;
//    @Autowired
//    ItemRepository itemRepository;
//    @Autowired
//    CartItemService cartItemService;
//    @Test
//    @Order(1)
//    void createCart() {
//        //given
//        GeneralMember user = GeneralMember.builder()
//                .username("testUser1")
//                .build();
//        GeneralMember generalMember = memberRepository.save(user);
//        //when
//        Long cartId = cartService.createCart(user);
//        //then
//        Cart cart = cartRepository.findById(cartId).orElseThrow();
//        assertThat(cart.getMember().getId()).isEqualTo(generalMember.getId());
//    }
//
//    @Test
//    @Order(2)
//    void deleteCart() {
//        //given
//        Item item = new Item();
//        itemRepository.save(item);
//
//        String username="testUser1";
//
//        cartItemService.putCart(username,1L,3);
//        //when
//        cartService.deleteCart(username);
//        //then
//        assertThrows(NoSuchElementException.class,
//                ()->cartRepository.findCartByUsername(username).orElseThrow());
//    }
//}
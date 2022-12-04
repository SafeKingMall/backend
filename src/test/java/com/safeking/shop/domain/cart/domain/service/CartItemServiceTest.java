package com.safeking.shop.domain.cart.domain.service;

import com.safeking.shop.domain.cart.domain.entity.CartItem;
import com.safeking.shop.domain.cart.domain.repository.CartItemRepository;
import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.item.domain.repository.ItemRepository;
import com.safeking.shop.domain.user.domain.entity.member.GeneralMember;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CartItemServiceTest {
    @Autowired
    CartItemService cartItemService;
    @Autowired
    CartItemRepository cartItemRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CartService cartService;
    @Autowired
    ItemRepository itemRepository;
    Long cartItemId;
    @BeforeAll
    public void init(){
        GeneralMember user = GeneralMember.builder()
                .username("testUser1")
                .build();

        memberRepository.save(user);

        cartService.createCart(user);

        for (int i = 1; i <= 3; i++) {
            Item item = new Item();
            itemRepository.save(item);
        }
    }
    @RepeatedTest(value = 3)
    @Order(1)
    void putCart(RepetitionInfo repetitionInfo) {
        //given
        String username="testUser1";
        Long itemId= Long.valueOf(repetitionInfo.getCurrentRepetition());
        int count=3;
        //when
        cartItemId = cartItemService.putCart(username, itemId, count);
        //then
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow();

        assertAll(
                 ()->assertThat(cartItem.getCart().getMember().getUsername()).isEqualTo(username)
                ,()->assertThat(cartItem.getItem().getId()).isEqualTo(itemId)
        );
    }

    @Test
    @Order(2)
    void updateCartItem() {
        //given
        String username="testUser1";
        Long itemId=cartItemId;
        int updateCount=3;
        //when
        cartItemService.updateCartItem("testUser1",itemId,updateCount);
        //then
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow();

        assertAll(
                ()->assertThat(cartItem.getCart().getMember().getUsername()).isEqualTo(username)
                ,()->assertThat(cartItem.getItem().getId()).isEqualTo(itemId)
                ,()->assertThat(cartItem.getCount()).isEqualTo(updateCount)
        );
    }

    @Test
    @Order(3)
    void deleteCartItemFromCart() {
        //given
        String username="testUser1";
        //when
        cartItemService.deleteCartItemFromCart(username,1L);
        //then
        assertThrows(NoSuchElementException.class,
                ()->cartItemRepository.findByItemIdAndUsername(1L,username).orElseThrow());

        //when
        cartItemService.deleteCartItemFromCart(username,2L,3L);
        //then
        assertThrows(NoSuchElementException.class,
                ()->cartItemRepository.findByItemIdAndUsername(2L,username).orElseThrow());
        assertThrows(NoSuchElementException.class,
                ()->cartItemRepository.findByItemIdAndUsername(3L,username).orElseThrow());


    }
}
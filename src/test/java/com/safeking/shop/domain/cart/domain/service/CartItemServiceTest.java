package com.safeking.shop.domain.cart.domain.service;

import com.safeking.shop.domain.cart.domain.entity.Cart;
import com.safeking.shop.domain.cart.domain.entity.CartItem;
import com.safeking.shop.domain.cart.domain.repository.CartItemRepository;
import com.safeking.shop.domain.cart.domain.repository.CartRepository;
import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.item.domain.repository.ItemRepository;
import com.safeking.shop.domain.user.domain.entity.member.GeneralMember;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@ActiveProfiles("test")
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
    @Autowired
    CartRepository cartRepository;

    @Test
    @DisplayName("1. 장바구니 담기")
    void putCart() {
        //init
        GeneralMember user = GeneralMember.builder()
                .username("TestUser1")
                .build();
        memberRepository.save(user);
        cartService.createCart(user);
        Item savedItem = itemRepository.save(new Item());

        //given
        String username="TestUser1";
        Long itemId=savedItem.getId();
        //when
        Long cartItemId = cartItemService.putCart(username, itemId, 3);
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow();
        assertAll(
                ()->assertThat(cartItem.getCart().getMember().getUsername()).isEqualTo(username)
                ,()->assertThat(cartItem.getItem().getId()).isEqualTo(itemId)
        );
    }

    @Test
    @DisplayName("2. 장바구니 아이템 수정하기")
    void updateCartItem() {
        //given
        GeneralMember user = GeneralMember.builder()
                .username("TestUser1")
                .build();
        memberRepository.save(user);
        cartService.createCart(user);
        Item savedItem = itemRepository.save(new Item());

        String username="TestUser1";
        Long itemId=savedItem.getId();

        Long cartItemId = cartItemService.putCart(username, itemId, 3);
        //when
        cartItemService.updateCartItem("TestUser1", cartItemId, 4);
        //then
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow();

        assertAll(
                ()->assertThat(cartItem.getCart().getMember().getUsername()).isEqualTo(username)
                ,()->assertThat(cartItem.getItem().getId()).isEqualTo(itemId)
                ,()->assertThat(cartItem.getCount()).isEqualTo(4)
        );
    }

    @Test
    @DisplayName("장바구니 아이템 삭제하기")
    void deleteCartItemFromCart() {
        //given
        GeneralMember user = GeneralMember.builder()
                .username("TestUser1")
                .build();
        memberRepository.save(user);
        cartService.createCart(user);
        Item savedItem = itemRepository.save(new Item());

        String username="TestUser1";
        Long itemId=savedItem.getId();

        Long cartItemId = cartItemService.putCart(username, itemId, 3);
        //when
        cartItemService.deleteCartItemFromCart(username,itemId);
        //then
        assertThrows(NoSuchElementException.class,
                ()->cartItemRepository.findByItemIdAndUsername(itemId,username).orElseThrow());
    }
}
package com.safeking.shop.domain.cart.domain.repository;

import com.safeking.shop.domain.cart.domain.entity.CartItem;
import com.safeking.shop.domain.cart.domain.service.CartItemService;
import com.safeking.shop.domain.cart.domain.service.CartService;
import com.safeking.shop.domain.cart.web.query.repository.CartQueryRepository;
import com.safeking.shop.domain.cart.web.response.CartItemResponse;
import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.item.domain.repository.ItemRepository;
import com.safeking.shop.domain.user.domain.entity.member.GeneralMember;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Commit
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CartItemRepositoryTest {

    @Autowired
    CartItemRepository cartItemRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CartQueryRepository cartQueryRepository;
    @Autowired
    CartService cartService;
    @Autowired
    CartItemService cartItemService;
    @Autowired
    ItemRepository itemRepository;
    @BeforeAll
    void init(){
        System.out.println("------------------------init------------------------");
        GeneralMember user = GeneralMember.builder()
                .username("testUser1")
                .build();
        GeneralMember user2 = GeneralMember.builder()
                .username("testUser2")
                .build();
        memberRepository.save(user);
        memberRepository.save(user2);

        cartService.createCart(user);
        cartService.createCart(user2);

        for (int i = 1; i <= 10; i++) {
            Item item = new Item();
            itemRepository.save(item);

            String username = i % 2 == 0 ? user.getUsername() : user2.getUsername();
            cartItemService.putCart(username,item.getId(),3);
        }
    }
    @Test
    void findByItemAndUsername() {
        //given
        //when
        CartItem cartItem = cartItemRepository.findByItemIdAndUsername(2L, "testUser1").orElseThrow();
        //then
        assertAll(
                ()->assertThat(cartItem.getItem().getId()).isEqualTo(2L),
                ()->assertThat(cartItem.getCart().getMember().getUsername()).isEqualTo("testUser1")
        );
    }
    @Test
    void deleteCartItemBatch(){
        //given
        Long cartId=1L;
        //when
        cartItemRepository.deleteCartItemBatch(cartId);
        //then
        List<CartItemResponse> cartList
                = cartQueryRepository.searchAll("testUser1", PageRequest.of(0, 5))
                .stream().collect(Collectors.toList());

        assertThat(cartList).isEmpty();
    }
    @Test
    void deleteCartItem(){
        //given
        Long cartId=2L;
        //when
        cartItemRepository.deleteCartItem(cartId,1L);
        //then
        assertThrows(NoSuchElementException.class,
                ()->cartItemRepository.findByCartIdAndItemId(cartId,1L).orElseThrow());
        //when
        cartItemRepository.deleteCartItem(cartId,3L,5L);
        //then
        assertThrows(NoSuchElementException.class,
                ()->cartItemRepository.findByCartIdAndItemId(cartId,3L).orElseThrow());
        assertThrows(NoSuchElementException.class,
                ()->cartItemRepository.findByCartIdAndItemId(cartId,5L).orElseThrow());


    }
}
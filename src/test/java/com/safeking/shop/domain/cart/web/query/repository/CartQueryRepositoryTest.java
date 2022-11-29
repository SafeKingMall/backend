package com.safeking.shop.domain.cart.web.query.repository;

import com.safeking.shop.domain.cart.domain.entity.Cart;
import com.safeking.shop.domain.cart.domain.repository.CartItemRepository;
import com.safeking.shop.domain.cart.domain.repository.CartRepository;
import com.safeking.shop.domain.cart.domain.service.CartService;
import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.item.domain.repository.ItemRepository;
import com.safeking.shop.domain.user.domain.entity.member.GeneralMember;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import com.safeking.shop.global.config.CustomBCryPasswordEncoder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@Commit
class CartQueryRepositoryTest {

    @Autowired CartQueryRepository cartQueryRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired CustomBCryPasswordEncoder encoder;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    CartItemRepository cartItemRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    CartService cartService;
    @Test
    void searchAll() {

        for (int i = 1; i <=10 ; i++) {
            Item item = new Item();
            item.setPrice(100);
            item.setName("item"+i);
            item.setQuantity(i);
            itemRepository.save(item);
        }

        Member user = GeneralMember.builder()
                .username("testUser")
                .password(encoder.encode("1234"))
                .roles("ROLE_USER").build();
        memberRepository.save(user);

        Member user2 = GeneralMember.builder()
                .username("testUser2")
                .password(encoder.encode("1234"))
                .roles("ROLE_USER").build();

        memberRepository.save(user);
        memberRepository.save(user2);


        Cart cart = new Cart(user);
        cartRepository.save(cart);

        cartService.putCart(user.getUsername(),1L,3);
        cartService.putCart(user.getUsername(),2L,3);
        cartService.putCart(user.getUsername(),3L,3);

        Cart cart2 = new Cart(user2);
        cartRepository.save(cart2);

        cartService.putCart(user2.getUsername(),4L,3);
        cartService.putCart(user2.getUsername(),5L,3);
        cartService.putCart(user2.getUsername(),6L,3);

        Long id = cartRepository.findCartByUsername(user2.getUsername()).orElseThrow().getId();

//        cartQueryRepository.searchAll(cart).stream().forEach(item-> System.out.println("item.getItemName() = " + item.getItemName()));



    }
}
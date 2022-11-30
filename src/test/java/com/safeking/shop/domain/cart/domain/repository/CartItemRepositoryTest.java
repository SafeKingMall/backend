package com.safeking.shop.domain.cart.domain.repository;

import com.safeking.shop.domain.cart.domain.entity.CartItem;
import com.safeking.shop.domain.cart.domain.service.CartItemService;
import com.safeking.shop.domain.cart.domain.service.CartService;
import com.safeking.shop.domain.user.domain.entity.member.GeneralMember;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Commit
class CartItemRepositoryTest {

    @Autowired CartItemRepository cartItemRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CartService cartService;
    @Autowired
    CartItemService cartItemService;
    @Test
    void findByItemAndUsername() {
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

        cartItemService.putCart(user.getUsername(),1L,3);
        cartItemService.putCart(user.getUsername(),2L,3);

        cartItemService.putCart(user2.getUsername(),2L,3);
        cartItemService.putCart(user2.getUsername(),3L,3);

        CartItem cartItem = cartItemRepository.findByItemIdAndUsername(1L, user.getUsername()).orElseThrow();
        System.out.println("cartItem.getId() = " + cartItem.getId());
    }
}
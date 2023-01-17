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
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
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
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CartItemRepositoryTest {
    /**
     * 1. @SpringBootTest: 모든 bean 을 생성 --> 밑에 @Autowired 가 가능해짐
     * 2. @ActiveProfiles("test"): @Profile("test") 인것은 실행, ex) @Profile("local") 은 실행이 안됨
     * 3. @Transactional: 테스트 시 마다 rollback 을 시켜줌, * 중요: identity 전략시에 id의 값은 rollback 이 안됨
     * 4. @TestInstance(TestInstance.Lifecycle.PER_CLASS): 변수를 사용할 수가 있게 된다.
     **/
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
    Long itemId1=null;
    Long itemId2=null;
    Long cartId=null;
    Long cartId2=null;
    @BeforeEach
    void init(){
        GeneralMember user = generateGeneralMember("USER1");
        GeneralMember user2 = generateGeneralMember("USER2");

        cartId = cartService.createCart(user);
        cartId2 = cartService.createCart(user2);

        itemId1= itemRepository.save(new Item()).getId();
        itemId2 = itemRepository.save(new Item()).getId();

        cartItemService.putCart(user.getUsername(),itemId1,3);
        cartItemService.putCart(user2.getUsername(),itemId2,4);
    }

    @Test
    void findByItemAndUsername() {
        //given
        //when
        CartItem cartItem = cartItemRepository.findByItemIdAndUsername(itemId1, "USER1").orElseThrow();
        //then
        assertAll(
                ()->assertThat(cartItem.getItem().getId()).isEqualTo(itemId1),
                ()->assertThat(cartItem.getCart().getMember().getUsername()).isEqualTo("USER1")
        );
    }
    @Test
    void deleteCartItemBatch(){
        //given
        Item item = itemRepository.save(new Item());
        cartItemService.putCart("USER1",item.getId(),3);

        //when
        cartItemRepository.deleteCartItemBatch(cartId);
        //then
        List<CartItemResponse> cartList
                = cartQueryRepository.searchAll("USER1", PageRequest.of(0, 5))
                .stream().collect(Collectors.toList());

        assertThat(cartList).isEmpty();
    }
    @Test
    void deleteCartItem(){
        //given
        //when
        cartItemRepository.deleteCartItem(cartId,itemId1);
        //then
        assertThrows(NoSuchElementException.class,
                ()->cartItemRepository.findByCartIdAndItemId(cartId,itemId1).orElseThrow());
    }

    private GeneralMember generateGeneralMember(String USER1) {
        GeneralMember user = getGeneralMember(USER1);
        memberRepository.save(user);
        return user;
    }

    private static GeneralMember getGeneralMember(String USER1) {
        GeneralMember user = GeneralMember.builder()
                .username(USER1)
                .build();
        return user;
    }
}
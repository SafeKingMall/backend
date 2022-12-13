package com.safeking.shop.domain.cart.web.query.repository;

import com.safeking.shop.domain.cart.domain.entity.Cart;
import com.safeking.shop.domain.cart.domain.repository.CartItemRepository;
import com.safeking.shop.domain.cart.domain.repository.CartRepository;
import com.safeking.shop.domain.cart.domain.service.CartItemService;
import com.safeking.shop.domain.cart.domain.service.CartService;
import com.safeking.shop.domain.cart.web.response.CartItemResponse;
import com.safeking.shop.domain.item.domain.entity.Category;
import com.safeking.shop.domain.item.domain.entity.CategoryItem;
import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.item.domain.repository.CategoryItemRepository;
import com.safeking.shop.domain.item.domain.repository.CategoryRepository;
import com.safeking.shop.domain.item.domain.repository.ItemRepository;
import com.safeking.shop.domain.user.domain.entity.member.GeneralMember;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import com.safeking.shop.global.CartHelper;
import com.safeking.shop.global.TestUserHelper;
import com.safeking.shop.global.config.CustomBCryPasswordEncoder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class CartQueryRepositoryTest {

    @Autowired CartQueryRepository cartQueryRepository;
    @Autowired CartHelper cartHelper;

    @Test
    void searchAll() {
        //given
        cartHelper.createTemporaryCartItem();
        //when
        Page<CartItemResponse> cartItemResponses = cartQueryRepository
                .searchAll(TestUserHelper.USER_USERNAME, PageRequest.of(0, 5));
        //then
        assertAll(
                ()->assertThat(cartItemResponses.getPageable().getOffset()).isEqualTo(0)
                ,()->assertThat(cartItemResponses.getPageable().getPageSize()).isEqualTo(5)
                ,()->assertThat(cartItemResponses.getPageable().getPageNumber()).isEqualTo(0)
                ,()->assertThat(cartItemResponses.getTotalElements()).isEqualTo(10)
                ,()->assertThat(cartItemResponses.getTotalPages()).isEqualTo(2)
                ,()->assertThat(cartItemResponses.getSize()).isEqualTo(5)
                ,()->assertThat(cartItemResponses.getNumberOfElements()).isEqualTo(5)
        );
    }
}
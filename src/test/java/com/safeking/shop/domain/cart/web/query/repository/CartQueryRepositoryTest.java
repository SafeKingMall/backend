//package com.safeking.shop.domain.cart.web.query.repository;
//
//import com.safeking.shop.domain.cart.domain.entity.Cart;
//import com.safeking.shop.domain.cart.domain.repository.CartItemRepository;
//import com.safeking.shop.domain.cart.domain.repository.CartRepository;
//import com.safeking.shop.domain.cart.domain.service.CartItemService;
//import com.safeking.shop.domain.cart.domain.service.CartService;
//import com.safeking.shop.domain.cart.web.response.CartItemResponse;
//import com.safeking.shop.domain.item.domain.entity.Category;
//import com.safeking.shop.domain.item.domain.entity.CategoryItem;
//import com.safeking.shop.domain.item.domain.entity.Item;
//import com.safeking.shop.domain.item.domain.repository.CategoryItemRepository;
//import com.safeking.shop.domain.item.domain.repository.CategoryRepository;
//import com.safeking.shop.domain.item.domain.repository.ItemRepository;
//import com.safeking.shop.domain.user.domain.entity.member.GeneralMember;
//import com.safeking.shop.domain.user.domain.entity.member.Member;
//import com.safeking.shop.domain.user.domain.repository.MemberRepository;
//import com.safeking.shop.global.config.CustomBCryPasswordEncoder;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.test.annotation.Commit;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.*;
//@SpringBootTest
//@Transactional
//@ActiveProfiles("test")
//class CartQueryRepositoryTest {
//
//    @Autowired CartQueryRepository cartQueryRepository;
//    @Autowired
//    MemberRepository memberRepository;
//    @Autowired CustomBCryPasswordEncoder encoder;
//    @Autowired
//    CartRepository cartRepository;
//    @Autowired
//    CartItemRepository cartItemRepository;
//    @Autowired
//    ItemRepository itemRepository;
//    @Autowired
//    CartService cartService;
//    @Autowired
//    CartItemService cartItemService;
//    @Autowired
//    CategoryItemRepository categoryItemRepository;
//    @Autowired
//    CategoryRepository categoryRepository;
//    @Test
//    void searchAll() {
//        //given
//        //카테고리를 생성
//        Category category1 = new Category("중대사고예방");
//        Category category2 = new Category("해양사고 예방");
//
//        //1. item 10개 생성
//        for (int i = 1; i <=10 ; i++) {
//            Item item = new Item();
//            item.setPrice(100);
//            item.setName("item"+i);
//            item.setQuantity(i);
//
//            Category category = i % 2 == 0 ?category1 : category2;
//
//            categoryRepository.save(category);
//            CategoryItem categoryItem = new CategoryItem(category, item);
//
//            itemRepository.save(item);
//            categoryItemRepository.save(categoryItem);
//        }
//        // categoryItem 생성
//        //2. member 2명 생성
//        Member user = GeneralMember.builder()
//                .username("testUser")
//                .password(encoder.encode("1234"))
//                .roles("ROLE_USER").build();
//        memberRepository.save(user);
//
//        Member user2 = GeneralMember.builder()
//                .username("testUser2")
//                .password(encoder.encode("1234"))
//                .roles("ROLE_USER").build();
//
//        memberRepository.save(user2);
//
//        //장바구니 생성
//        cartService.createCart(user);
//        cartService.createCart(user2);
//
//        //장바구니에 아이템 담기
//        cartItemService.putCart(user.getUsername(),1L,3);
//        cartItemService.putCart(user.getUsername(),2L,3);
//        cartItemService.putCart(user.getUsername(),3L,3);
//
//        cartItemService.putCart(user2.getUsername(),4L,3);
//        cartItemService.putCart(user2.getUsername(),5L,3);
//        cartItemService.putCart(user2.getUsername(),6L,3);
//        //when then
//        List<CartItemResponse> cartItems = cartQueryRepository
//                .searchAll(user.getUsername(), PageRequest.of(0, 5))
//                .stream().collect(Collectors.toList());
//        cartItems.stream().forEach(cartItemResponse -> System.out.println("cartItemResponse.() = " + cartItemResponse.getCategoryName()));
//        cartItems.stream().forEach(cartItemResponse -> System.out.println("cartItemResponse.getCategoryName() = " + cartItemResponse.getItemName()));
//
//    }
//}
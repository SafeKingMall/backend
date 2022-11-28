package com.safeking.shop.domain.cart.web.query.service;

import com.safeking.shop.domain.cart.domain.entity.Cart;
import com.safeking.shop.domain.cart.domain.repository.CartRepository;
import com.safeking.shop.domain.cart.web.query.repository.CartQueryRepository;
import com.safeking.shop.domain.cart.web.response.CartItemResponse;
import com.safeking.shop.domain.cart.web.response.CartResponse;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import com.safeking.shop.global.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartQueryService {

    private final CartQueryRepository cartQueryRepository;

    private final CartRepository cartRepository;

    public CartResponse showCart(String username){

        List<CartItemResponse> cartItemResponses = cartQueryRepository.searchAll(username);

        return CartResponse.builder()
                .cartItemResponses(cartItemResponses)
                .deliveryFee(Cart.DELIVERY_FEE)
                .totalPrice(
                        cartItemResponses
                                .stream()
                                .mapToInt(cartItem -> cartItem.getItemPrice()).sum())
                                .build();
    }

}

package com.safeking.shop.domain.cart.web.query.service;

import com.safeking.shop.domain.cart.domain.entity.Cart;
import com.safeking.shop.domain.cart.domain.repository.CartRepository;
import com.safeking.shop.domain.cart.web.query.repository.CartQueryRepository;
import com.safeking.shop.domain.cart.web.response.CartItemResponse;
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

    public List<CartItemResponse> showCart(String username){

        Cart cart = cartRepository.findCartByUsername(username).orElseThrow(() -> new EntityNotFoundException("회원과 일치하는 장바구니가 없습니다."));

        return cartQueryRepository.searchAll(cart);
    }

}

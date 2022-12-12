package com.safeking.shop.domain.cart.domain.service;

import com.safeking.shop.domain.cart.domain.entity.Cart;
import com.safeking.shop.domain.cart.domain.entity.CartItem;
import com.safeking.shop.domain.cart.domain.repository.CartItemRepository;
import com.safeking.shop.domain.cart.domain.repository.CartRepository;
import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.item.domain.repository.ItemRepository;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import com.safeking.shop.global.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.rmi.AlreadyBoundException;
import java.util.Arrays;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ItemRepository itemRepository;


    public Long createCart(Member member){
        if(cartRepository.findCartByUsername(member.getUsername()).orElse(null) !=null)
            throw new EntityExistsException("이미 장바구니가 존재합니다.");

        Cart cart = new Cart(member);
        cartRepository.save(cart);

        return cart.getId();
    }

    public void deleteCart(String username){
        Cart cart = cartRepository
                .findCartByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("장바구니가 존재하지 않습니다."));

        cartItemRepository.deleteCartItemBatch(cart.getId());
        cartRepository.delete(cart);
    }

}

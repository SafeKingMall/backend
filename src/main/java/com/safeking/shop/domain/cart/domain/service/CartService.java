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
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;


    public Long createCart(String username){
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new MemberNotFoundException("회원이 존재하지 않습니다."));

        if(cartRepository.findByMember(member).orElse(null) !=null)
            throw new EntityExistsException("이미 장바구니가 존재합니다.");

        Cart cart = new Cart(member);
        cartRepository.save(cart);

        return cart.getId();
    }

    public void putCart(String username, Long itemId, int count){
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new EntityNotFoundException("아이템이 존재하지 않습니다."));

        Cart cart = getCartForUsername(username);

        CartItem cartItem = cartItemRepository.findByItemAndCart(item, cart).orElse(null);

        if (cartItem != null) throw new EntityExistsException("동일한 상품이 장바구니에 있습니다.");

        CartItem newCartItem = new CartItem(item, cart, count);
        cartItemRepository.save(newCartItem);
    }

    public void deleteCartItemFromCart(String username, Long... itemId){

        Cart cart = cartRepository.findCartByUsername(username).orElseThrow(() -> new EntityNotFoundException("장바구니가 없습니다."));
        if(itemId!=null) cartItemRepository.deleteCartItem(cart.getId(), itemId);

    }

    public void updateCartItem(String username, Long itemId, int count){
        CartItem cartItem = findCartItem(username, itemId).orElseThrow(()->new EntityNotFoundException("찾으시는 장바구니 아이템이 없습니다."));

        cartItem.changeCount(count);
    }

    private Optional<CartItem> findCartItem(String username, Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new EntityNotFoundException("아이템이 존재하지 않습니다."));

        Cart cart = getCartForUsername(username);

        CartItem cartItem = cartItemRepository.findByItemAndCart(item, cart).orElseThrow(() -> new EntityNotFoundException("찾으시는 아이템이 없습니다."));
        return Optional.ofNullable(cartItem);
    }


    private Cart getCartForUsername(String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new MemberNotFoundException("회원이 존재하지 않습니다."));

        Cart cart = cartRepository.findByMember(member).orElseThrow(() -> new EntityNotFoundException("장바구니가 존재하지 않습니다."));
        return cart;
    }

    public void deleteCart(String username){
        Cart cart = cartRepository.findCartByUsername(username).orElseThrow(() -> new EntityNotFoundException("장바구니가 존재하지 않습니다."));

        cartItemRepository.deleteCartItemBatch(cart.getId());
        cartRepository.delete(cart);
    }

}

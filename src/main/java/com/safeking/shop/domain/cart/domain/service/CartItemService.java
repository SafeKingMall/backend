package com.safeking.shop.domain.cart.domain.service;

import com.safeking.shop.domain.cart.domain.entity.Cart;
import com.safeking.shop.domain.cart.domain.entity.CartItem;
import com.safeking.shop.domain.cart.domain.repository.CartItemRepository;
import com.safeking.shop.domain.cart.domain.repository.CartRepository;
import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.item.domain.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
@Service
@Transactional
@RequiredArgsConstructor
public class CartItemService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ItemRepository itemRepository;

    public Long putCart(String username, Long itemId, int count){
        Cart cart = cartRepository
                .findCartByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("장바구니가 없습니다."));

        Item item = itemRepository
                .findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("아이템이 없습니다."));

        //기존 장바구니에 같은 아이템이 있는 지 확인
        CartItem cartItem = cartItemRepository
                .findByItemIdAndUsername(itemId,username)
                .orElse(null);

        if (cartItem != null) throw new EntityExistsException("동일한 상품이 장바구니에 있습니다.");

        CartItem newCartItem = CartItem.createCartItem(item, cart, count);

        cartItemRepository.save(newCartItem);
        return newCartItem.getId();
    }

    public void updateCartItem(String username, Long itemId, int count){
        CartItem cartItem
                = cartItemRepository
                .findByItemIdAndUsername(itemId, username)
                .orElseThrow(() -> new EntityNotFoundException("장바구니아이템이 존재하지 않습니다."));

        cartItem.changeCount(count);
    }

    public void deleteCartItemFromCart(String username, Long... itemId){

        Cart cart = cartRepository
                .findCartByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("장바구니가 없습니다."));

        if(itemId!=null) cartItemRepository.deleteCartItem(cart.getId(), itemId);
    }

}

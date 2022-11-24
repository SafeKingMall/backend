package com.safeking.shop.domain.cart.web.query.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.safeking.shop.domain.cart.domain.entity.Cart;
import com.safeking.shop.domain.cart.domain.entity.CartItem;
import com.safeking.shop.domain.cart.domain.entity.QCart;
import com.safeking.shop.domain.cart.domain.entity.QCartItem;
import com.safeking.shop.domain.cart.web.response.CartItemResponse;
import com.safeking.shop.domain.cart.web.response.QCartItemResponse;
import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.item.domain.entity.QItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.safeking.shop.domain.cart.domain.entity.QCart.cart;
import static com.safeking.shop.domain.cart.domain.entity.QCartItem.*;
import static com.safeking.shop.domain.item.domain.entity.QItem.item;

@Repository
@RequiredArgsConstructor
public class CartQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<CartItemResponse> searchAll(Cart cart){

        return queryFactory
                .select(new QCartItemResponse(item.name,item.price,item.quantity))
                .from(cartItem)
                .join(cartItem.item,item)
                .where(cartItem.cart.eq(cart))
                .fetch();
    }
}

package com.safeking.shop.domain.cart.web.query.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.safeking.shop.domain.cart.domain.entity.Cart;
import com.safeking.shop.domain.cart.domain.entity.CartItem;
import com.safeking.shop.domain.cart.domain.entity.QCart;
import com.safeking.shop.domain.cart.domain.entity.QCartItem;
import com.safeking.shop.domain.cart.web.response.CartItemResponse;
import com.safeking.shop.domain.cart.web.response.QCartItemResponse;
import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.item.domain.entity.QCategory;
import com.safeking.shop.domain.item.domain.entity.QCategoryItem;
import com.safeking.shop.domain.item.domain.entity.QItem;
import com.safeking.shop.domain.user.domain.entity.member.QMember;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.safeking.shop.domain.cart.domain.entity.QCart.*;
import static com.safeking.shop.domain.cart.domain.entity.QCart.cart;
import static com.safeking.shop.domain.cart.domain.entity.QCartItem.*;
import static com.safeking.shop.domain.item.domain.entity.QCategory.*;
import static com.safeking.shop.domain.item.domain.entity.QCategoryItem.*;
import static com.safeking.shop.domain.item.domain.entity.QItem.item;
import static com.safeking.shop.domain.user.domain.entity.member.QMember.*;

@Repository
@RequiredArgsConstructor
public class CartQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Page<CartItemResponse> searchAll(String username, Pageable pageable){
        List<CartItemResponse> result = queryFactory
                .select(new QCartItemResponse(item.id, item.name, item.price, item.quantity,category.name))
                .from(cartItem,categoryItem)
                .join(cartItem.item, item)
                .join(cartItem.cart, cart)
                .join(cart.member, member)
                .join(categoryItem.category,category)
                .where(member.username.eq(username))
                .orderBy(cartItem.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> CountQuery = queryFactory
                .select(cartItem.count())
                .from(cartItem,categoryItem)
                .join(cartItem.item, item)
                .join(cartItem.cart, cart)
                .join(cart.member, member)
                .join(categoryItem.category,category)
                .where(member.username.eq(username));

        return PageableExecutionUtils.getPage(result,pageable,CountQuery::fetchOne);
    }

}
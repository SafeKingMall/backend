package com.safeking.shop.domain.cart.web.query.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.safeking.shop.domain.cart.web.response.CartItemResponse;
import com.safeking.shop.domain.cart.web.response.QCartItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.safeking.shop.domain.cart.domain.entity.QCart.cart;
import static com.safeking.shop.domain.cart.domain.entity.QCartItem.cartItem;
import static com.safeking.shop.domain.item.domain.entity.QCategory.category;
import static com.safeking.shop.domain.item.domain.entity.QCategoryItem.categoryItem;
import static com.safeking.shop.domain.item.domain.entity.QItem.item;
import static com.safeking.shop.domain.user.domain.entity.member.QMember.member;

@Repository
@RequiredArgsConstructor
public class CartQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Page<CartItemResponse> searchAll(String username, Pageable pageable){
        List<CartItemResponse> result = queryFactory
                .select(new QCartItemResponse(item.id, item.name, item.price, item.quantity, categoryItem.category.name))
                .from(cartItem)
                .join(cartItem.item, item)
                .join(cartItem.cart, cart)
                .join(cart.member, member)
                .innerJoin(categoryItem).on(item.eq(categoryItem.item))
                .where(member.username.eq(username))
                .orderBy(cartItem.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                ;

        JPAQuery<Long> CountQuery = queryFactory
                .select(cartItem.count())
                .from(cartItem)
                .join(cartItem.item, item)
                .join(cartItem.cart, cart)
                .join(cart.member, member)
                .where(member.username.eq(username))
                ;

        return PageableExecutionUtils.getPage(result,pageable,CountQuery::fetchOne);
    }

}

package com.safeking.shop.domain.cart.domain.entity;

import com.safeking.shop.domain.common.BaseTimeEntity;
import com.safeking.shop.domain.item.domain.entity.Item;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItem extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    private int count;

    public static CartItem createCartItem(Item item, Cart cart, int count) {
        CartItem cartItem = new CartItem();

        cartItem.count = count;
        cartItem.item = item;
        cartItem.cart = cart;

        cart.addCartItem(cartItem);
        return cartItem;
    }

    public void changeCount(int count) {
        this.count = count;
    }
}

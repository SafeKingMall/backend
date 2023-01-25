package com.safeking.shop.domain.cart.domain.entity;

import com.safeking.shop.domain.common.BaseTimeEntity;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart extends BaseTimeEntity {
    public static final int DELIVERY_FEE = 3000;
    public static final int LIMIT_COUNT = 19;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "cart")
    private List<CartItem> cartItems=new ArrayList<>();


    public Cart(Member member){
        this.member=member;
    }

    public void addCartItem(CartItem cartItem){
        if (cartItems.size() > LIMIT_COUNT) throw new IllegalArgumentException("최대 담을 수 있는 아이템 개수는 "+LIMIT_COUNT+"개 입니다.");

        this.cartItems.add(cartItem);
    }

    public void deleteCartItem(List<Long> itemId) {
        cartItems.removeIf(ci -> itemId.contains(ci.getId()));
    }
}


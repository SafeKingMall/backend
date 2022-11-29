package com.safeking.shop.domain.cart.domain.repository;

import com.safeking.shop.domain.cart.domain.entity.Cart;
import com.safeking.shop.domain.cart.domain.entity.CartItem;
import com.safeking.shop.domain.item.domain.entity.Item;
import org.apache.log4j.spi.OptionHandler;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    Optional<CartItem> findByItemAndCart(Item item, Cart cart);

    @Modifying
    @Query("delete from CartItem c where c.cart.id=:cartId")
    void deleteCartItemBatch(@Param("cartId") Long cartId);
    @Modifying
    @Query("delete from CartItem c where c.item.id in :ItemIds and c.cart.id=:CartId")
    void deleteCartItem(@Param("CartId") Long cartId ,@Param("ItemIds") Long... itemIds);
}

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
import org.springframework.security.core.parameters.P;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    @Query("select ci from CartItem ci join fetch ci.cart c join fetch c.member m where ci.item.id=:itemId and m.username =:username")
    Optional<CartItem> findByItemIdAndUsername(@Param("itemId") Long itemId, @Param("username") String username);

    @Modifying
    @Query("delete from CartItem c where c.cart.id=:cartId")
    void deleteCartItemBatch(@Param("cartId") Long cartId);
    @Modifying
    @Query("delete from CartItem c where c.item.id in :ItemIds and c.cart.id=:CartId")
    void deleteCartItem(@Param("CartId") Long cartId ,@Param("ItemIds") Long... itemIds);

    @Query("select ci from CartItem ci join fetch ci.cart c join fetch ci.item i where c.id=:cartId and i.id=:itemId")
    Optional<CartItem> findByCartIdAndItemId(@Param("cartId") Long cartId, @Param("itemId") Long itemId);
}

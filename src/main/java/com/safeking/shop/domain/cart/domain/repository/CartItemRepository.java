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

import java.util.List;
import java.util.Optional;

/**
 * <CQS>
 * cud 쿼리는 @Query 사용
 * 조회 쿼리는 queryDsl 사용
 **/
public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    /**
     * 중간 테이블의 join
     * 초기화가 필요한 부분만 join fetch 사용
     **/
    @Query("select ci from CartItem ci join fetch ci.cart c join fetch c.member m where ci.item.id=:itemId and m.username =:username")
    Optional<CartItem> findByItemIdAndUsername(@Param("itemId") Long itemId, @Param("username") String username);
    /**
     * @Modifying 사용: bulk 수정 쿼리
     **/
    @Modifying
    @Query("delete from CartItem c where c.cart.id=:cartId")
    void deleteCartItemBatch(@Param("cartId") Long cartId);
    /**
     * In 사용: 최적화
     **/
    @Modifying
    @Query("delete from CartItem c where c.item.id in :ItemIds and c.cart.id=:CartId")
    void deleteCartItem(@Param("CartId") Long cartId ,@Param("ItemIds") Long... itemIds);

    @Query("select ci from CartItem ci join fetch ci.cart c join fetch ci.item i where c.id=:cartId and i.id=:itemId")
    Optional<CartItem> findByCartIdAndItemId(@Param("cartId") Long cartId, @Param("itemId") Long itemId);


}

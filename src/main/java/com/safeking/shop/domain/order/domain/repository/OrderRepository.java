package com.safeking.shop.domain.order.domain.repository;

import com.safeking.shop.domain.order.domain.entity.Order;
import com.safeking.shop.domain.order.web.query.repository.OrderRepositoryCustom;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {
    @Query("select o from Order o" +
            " join fetch o.delivery d" +
            " where o.id = :id")
    Optional<Order> findOrder(@Param("id") Long id);
    @Query("select o from Order o" +
            " join fetch o.delivery d" +
            " join fetch o.orderItems oi" +
            " join fetch o.safeKingPayment p" +
            " join fetch oi.item i" +
            " join fetch ItemPhoto ip on i.id = ip.id" +
            " where o.id = :id")
    Optional<Order> findOrderDetail(@Param("id") Long id);
//    @Query("select o from Order o" +
//            " join fetch o.safeKingPayment sp" +
//            " where o.merchantUid = :merchantUid")
//    Optional<Order> findOrderForCancel(@Param("merchantUid") String merchantUid);
    Optional<Order> findOrderByMerchantUid(String merchantUid);
    @EntityGraph(attributePaths = {"delivery", "safeKingPayment"})
    List<Order> findByMember(Member member);
    @Modifying
    @Query("delete from Order o where o in :orderList")
    void deleteAllByMemberBatch(@Param("orderList") List<Order> orderList);
}

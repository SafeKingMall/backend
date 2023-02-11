package com.safeking.shop.domain.order.domain.repository;

import com.safeking.shop.domain.order.domain.entity.Order;
import com.safeking.shop.domain.order.web.query.repository.OrderRepositoryCustom;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {
    @Query("select o from Order o" +
            " join fetch o.delivery d" +
            " where o.id = :orderId" +
            " and o.member.id = :memberId")
    Optional<Order> findOrderByUser(@Param("orderId") Long orderId, @Param("memberId") Long memberId); // 주문(배송) 조회

    @Query("select o from Order o" +
            " join fetch o.delivery d" +
            " join fetch o.orderItems oi" +
            " join fetch o.safeKingPayment sp" +
            " join fetch oi.item i" +
            " join fetch ItemPhoto ip on ip.item = i" +
            " where o.id = :orderId" +
            " and o.member.id = :memberId")
    Optional<Order> findOrderDetailByUser(@Param("orderId") Long orderId, @Param("memberId") Long memberId); // 주문상세 내역

    @Query("select o from Order o" +
            " join fetch o.member m" +
            " join fetch o.delivery d" +
            " join fetch o.orderItems oi" +
            " join fetch o.safeKingPayment sp" +
            " join fetch oi.item i" +
            " join fetch ItemPhoto ip on ip.item = i" +
            " where o.id = :orderId")
    Optional<Order> findOrderDetailByAdmin(@Param("orderId") Long orderId); // 주문 상세 조회(어드민)

    Optional<Order> findOrderByMerchantUid(String merchantUid);

    @EntityGraph(attributePaths = {"delivery", "safeKingPayment"})
    List<Order> findByMember(Member member);

    @Modifying
    @Query("delete from Order o where o in :orderList")
    void deleteAllByMemberBatch(@Param("orderList") List<Order> orderList);

    @Query("select o from Order o" +
            " join fetch o.delivery d" +
            " join fetch o.safeKingPayment sf" +
            " join fetch o.orderItems oi" +
            " join fetch oi.item i" +
            " join fetch ItemPhoto ip on ip.item = i" +
            " where o.id = :orderId" +
            " and o.member.id = :memberId" +
            " and o.safeKingPayment.status <> 'CANCEL'" +
            " and o.status <> 'CANCEL'")
    Optional<Order> findOrderAskPaymentCancelByUser(@Param("orderId") Long orderId, @Param("memberId") Long memberId); // 환불신청 단건 조회

    @Query("select o from Order o" +
            " join fetch o.delivery d" +
            " join fetch o.orderItems oi" +
            " join fetch o.safeKingPayment sp" +
            " join fetch oi.item i" +
            " join fetch ItemPhoto ip on ip.item = i" +
            " where o.id = :orderId" +
            " and o.member.id = :memberId" +
            " and o.safeKingPayment.status = 'CANCEL'" +
            " and o.status = 'CANCEL'")
    Optional<Order> findOrderPaymentCancelDetailByUser(@Param("orderId") Long orderId, @Param("memberId") Long memberId); // 환불 상세 내역
}

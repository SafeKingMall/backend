package com.safeking.shop.domain.order.domain.repository;

import com.safeking.shop.domain.order.domain.entity.Order;
import com.safeking.shop.domain.order.domain.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrder(Order order);

    @Query("select oi from OrderItem oi where oi.order in :orderList")
    List<OrderItem> findByOrderList(@Param("orderList") List<Order> order);
    @Modifying
    @Query("delete from OrderItem oi where oi in :orderItems")
    void deleteByOrderBatch(@Param("orderItems") List<OrderItem> orderItems);

    /**
     * 참고: 벌크 연산은 영속성 컨텍스트를 무시하고 실행하기 때문에,
     * 영속성 컨텍스트에 있는 엔티티의 상태와 DB에 엔티티 상태가 달라질 수 있다.
     *
     * > 권장 방안
     * > 1. 영속성 컨텍스트에 엔티티가 없는 상태에서 벌크 연산을 먼저 실행한다.
     * > 2. 부득이하게 영속성 컨텍스트에 엔티티가 있으면 벌크 연산 직후 영속성 컨텍스트를 초기화 한다.
     */
    @Modifying(clearAutomatically = true) // 벌크성 쿼리 실행 후 영속성 컨텍스트 초기화
    @Query("delete from OrderItem oi where oi in :orderItems")
    void deleteAllByOrderItems(@Param("orderItems") List<OrderItem> orderItems);

}

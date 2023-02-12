package com.safeking.shop.domain.order.web.query.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.safeking.shop.domain.exception.OrderException;
import com.safeking.shop.domain.order.domain.entity.Order;
import com.safeking.shop.domain.order.domain.entity.status.DeliveryStatus;
import com.safeking.shop.domain.order.domain.entity.status.OrderStatus;
import com.safeking.shop.domain.order.web.query.repository.querydto.*;
import com.safeking.shop.domain.payment.domain.entity.PaymentStatus;
import com.safeking.shop.domain.order.web.dto.request.user.search.OrderSearchCondition;
import com.safeking.shop.domain.payment.web.client.dto.request.PaymentSearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.safeking.shop.domain.item.domain.entity.QItem.item;
import static com.safeking.shop.domain.order.constant.OrderConst.*;
import static com.safeking.shop.domain.order.domain.entity.QDelivery.delivery;
import static com.safeking.shop.domain.order.domain.entity.QOrder.order;
import static com.safeking.shop.domain.order.domain.entity.QOrderItem.orderItem;
import static com.safeking.shop.domain.payment.domain.entity.QSafekingPayment.safekingPayment;
import static com.safeking.shop.domain.user.domain.entity.member.QMember.member;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.springframework.util.StringUtils.*;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /**
     * 컬렉션을 페치 조인하면 페이징 불가...
     *
     * ToOne관계를 페치 조인
     * 지연 로딩 성능 최적화를 위해 hibernate.default_batch_fetch_size , @BatchSize 를 적용
     *  -> 이 옵션을 사용하면 컬렉션이나, 프록시 객체를 한꺼번에 설정한 size 만큼 IN 쿼리로 조회
     */
    @Override
    public Page<Order> findOrdersByUser(Pageable pageable, OrderSearchCondition condition, Long memberId) {
        List<Order> content = queryFactory
                .selectFrom(order)
                //.leftJoin(order.orderItems, orderItem).fetchJoin()
                .leftJoin(order.safeKingPayment, safekingPayment).fetchJoin()
                .leftJoin(order.delivery, delivery).fetchJoin()
                //.leftJoin(orderItem.item, item).fetchJoin()
                .where(
                        order.member.id.eq(memberId),
                        orderBetweenDate(condition.getFromDate(), condition.getToDate()),
                        keywordContains(condition.getKeyword()),
                        deliveryStatusEq(condition.getDeliveryStatus()),
                        paymentStatusEq(condition.getPaymentStatus()),
                        orderStatusEq(condition.getOrderStatus())
                )
                .orderBy(order.createDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(order.count())
                .from(order)
                //.leftJoin(order.orderItems, orderItem)
                .leftJoin(order.safeKingPayment, safekingPayment)
                .leftJoin(order.delivery, delivery)
                //.leftJoin(orderItem.item, item)
                .where(
                        order.member.id.eq(memberId),
                        orderBetweenDate(condition.getFromDate(), condition.getToDate()),
                        keywordContains(condition.getKeyword()),
                        deliveryStatusEq(condition.getDeliveryStatus()),
                        paymentStatusEq(condition.getPaymentStatus()),
                        orderStatusEq(condition.getOrderStatus())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    /**
     * 컬렉션을 페치 조인하면 페이징 불가...
     *
     * ToOne관계를 페치 조인
     * 지연 로딩 성능 최적화를 위해 hibernate.default_batch_fetch_size , @BatchSize 를 적용
     *  -> 이 옵션을 사용하면 컬렉션이나, 프록시 객체를 한꺼번에 설정한 size 만큼 IN 쿼리로 조회
     */
    @Override
    public Page<AdminOrderListQueryDto> findOrdersByAdmin(Pageable pageable, OrderSearchCondition condition) {

        List<AdminOrderListQueryDto> content = queryFactory
                .select(new QAdminOrderListQueryDto(order.id,
                        order.status.stringValue(),
                        order.safeKingPayment.amount,
                        order.createDate,
                        order.merchantUid,
                        new QAdminOrderListPaymentQueryDto(order.safeKingPayment.status.stringValue()),
                        new QAdminOrderListMemberQueryDto(order.member.name),
                        new QAdminOrderListDeliveryQueryDto(order.delivery.receiver, order.delivery.status.stringValue()))
                )
                .from(order)
                //.leftJoin(order.orderItems, orderItem)
                .leftJoin(order.safeKingPayment, safekingPayment)
                .leftJoin(order.delivery, delivery)
                .leftJoin(order.member, member)
                //.leftJoin(orderItem.item, item)
                .where(
                        orderBetweenDate(condition.getFromDate(), condition.getToDate()),
                        deliveryStatusEq(condition.getDeliveryStatus()),
                        paymentStatusEq(condition.getPaymentStatus())
                )
                .orderBy(order.createDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        content.forEach(order -> {
            List<AdminOrderListOrderItemQueryDto> orderItems = findOrderItemsByAdmin(order.getId(), condition.getKeyword());
            order.setOrderItems(orderItems);
        });

        JPAQuery<Long> countQuery = getFindOrdersByAdminCountQuery(condition);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private JPAQuery<Long> getFindOrdersByAdminCountQuery(OrderSearchCondition condition) {
        if(hasText(condition.getKeyword())) {
            return queryFactory
                    .select(order.count())
                    .from(order)
                    .leftJoin(order.orderItems, orderItem)
                    .leftJoin(orderItem.item, item)
                    .where(
                            keywordContains(condition.getKeyword())
                    );
        }

        return queryFactory
                .select(order.count())
                .from(order)
                .leftJoin(order.safeKingPayment, safekingPayment)
                .leftJoin(order.delivery, delivery)
                .leftJoin(order.member, member)
                .where(
                        orderBetweenDate(condition.getFromDate(), condition.getToDate()),
                        deliveryStatusEq(condition.getDeliveryStatus()),
                        paymentStatusEq(condition.getPaymentStatus())
                );
    }

    @Override
    public List<AdminOrderListOrderItemQueryDto> findOrderItemsByAdmin(Long orderId, String keyword) {
        return queryFactory.select(new QAdminOrderListOrderItemQueryDto(orderItem.id, orderItem.item.name))
                .from(orderItem)
                .leftJoin(orderItem.item, item)
                .where(
                        orderItem.order.id.eq(orderId),
                        keywordContains(keyword)
                )
                .fetch();
    }

    /**
     * 컬렉션을 페치 조인하면 페이징 불가...
     *
     * ToOne관계를 페치 조인
     * 지연 로딩 성능 최적화를 위해 hibernate.default_batch_fetch_size , @BatchSize 를 적용
     *  -> 이 옵션을 사용하면 컬렉션이나, 프록시 객체를 한꺼번에 설정한 size 만큼 IN 쿼리로 조회
     */
    @Override
    public Page<Order> findOrdersCancelByUser(Pageable pageable, PaymentSearchCondition condition, Long memberId) {
        List<Order> content = queryFactory
                .selectFrom(order)
                .leftJoin(order.safeKingPayment, safekingPayment).fetchJoin()
                .leftJoin(order.member, member).fetchJoin()
                .where(
                        order.member.id.eq(memberId),
                        paymentBetweenDate(condition.getFromDate(), condition.getToDate()),
                        paymentStatusEq(condition.getPaymentStatus())
                )
                .orderBy(order.safeKingPayment.cancelledAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(order.count())
                .from(order)
                .leftJoin(order.safeKingPayment, safekingPayment)
                .leftJoin(order.member, member)
                .where(
                        order.member.id.eq(memberId),
                        paymentBetweenDate(condition.getFromDate(), condition.getToDate()),
                        paymentStatusEq(condition.getPaymentStatus())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    // 결제 상태 조건
    private BooleanExpression paymentStatusEq(String paymentStats) {
        try {
            return hasText(paymentStats) ? order.safeKingPayment.status.eq(PaymentStatus.valueOf(paymentStats)) : null;
        } catch (IllegalArgumentException e) {
            throw new OrderException(ORDER_LIST_FIND_FAIL_PAYMENT_STATUS);
        }
    }

    // 배송 상태 조건
    private BooleanExpression deliveryStatusEq(String deliveryStatus) {
        try {
            return hasText(deliveryStatus) ? order.delivery.status.eq(DeliveryStatus.valueOf(deliveryStatus)) : null;
        } catch (IllegalArgumentException e) {
            throw new OrderException(ORDER_LIST_FIND_FAIL_DELIVERY_STATUS);
        }
    }

    // 주문 상태 조건
    private BooleanExpression orderStatusEq(String orderStatus) {
        try {
            return hasText(orderStatus) ? order.status.eq(OrderStatus.valueOf(orderStatus)) : null;
        } catch (IllegalArgumentException e) {
            throw new OrderException(ORDER_LIST_FIND_FAIL_ORDER_STATUS);
        }
    }

    // 아이템이름 포함 조건
    private BooleanExpression keywordContains(String keyword) {
        return hasText(keyword) ? item.name.contains(keyword) : null;
    }

    // 주문 일시 조건
    private BooleanExpression orderBetweenDate(String fromDate, String toDate) {

        //검색 시작, 종료 주문일시 둘다 있을 경우
        if(hasText(fromDate) && hasText(toDate)) {
            LocalDateTime from = LocalDate.parse(fromDate, ofPattern("yyyy-MM-dd")).atTime(0, 0, 0);
            LocalDateTime to = LocalDate.parse(toDate, ofPattern("yyyy-MM-dd")).atTime(23, 59, 59);

            return order.createDate.between(from, to);
        }

        //시작 주문일시만 있을 경우
        else if (hasText(fromDate)) {
            LocalDateTime from = LocalDate.parse(fromDate, ofPattern("yyyy-MM-dd")).atTime(0, 0, 0);
            LocalDateTime to = LocalDateTime.now();

            return order.createDate.between(from, to);
        }

        //종료 주문일시만 있을 경우
        else if(hasText(toDate)) {
            LocalDateTime from = LocalDateTime.now();
            LocalDateTime to = LocalDate.parse(toDate, ofPattern("yyyy-MM-dd")).atTime(23, 59, 59);

            return order.createDate.between(from, to);
        }

        return order.createDate.between(null, LocalDateTime.now());
    }

    // 결제 취소 일시 조건
    private BooleanExpression paymentBetweenDate(String fromDate, String toDate) {

        //검색 시작, 종료 주문일시 둘다 있을 경우
        if(hasText(fromDate) && hasText(toDate)) {
            LocalDateTime from = LocalDate.parse(fromDate, ofPattern("yyyy-MM-dd")).atTime(0, 0, 0);
            LocalDateTime to = LocalDate.parse(toDate, ofPattern("yyyy-MM-dd")).atTime(23, 59, 59);

            return order.safeKingPayment.cancelledAt.between(from, to);
        }

        //시작 주문일시만 있을 경우
        else if (hasText(fromDate)) {
            LocalDateTime from = LocalDate.parse(fromDate, ofPattern("yyyy-MM-dd")).atTime(0, 0, 0);
            LocalDateTime to = LocalDateTime.now();

            return order.safeKingPayment.cancelledAt.between(from, to);
        }

        //종료 주문일시만 있을 경우
        else if(hasText(toDate)) {
            LocalDateTime from = LocalDateTime.now();
            LocalDateTime to = LocalDate.parse(toDate, ofPattern("yyyy-MM-dd")).atTime(23, 59, 59);

            return order.safeKingPayment.cancelledAt.between(from, to);
        }

        return order.safeKingPayment.cancelledAt.between(null, LocalDateTime.now());
    }
}

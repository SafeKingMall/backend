package com.safeking.shop.domain.order.web.query.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.safeking.shop.domain.order.domain.entity.Order;
import com.safeking.shop.domain.order.domain.entity.status.DeliveryStatus;
import com.safeking.shop.domain.payment.domain.entity.PaymentStatus;
import com.safeking.shop.domain.order.web.dto.request.user.search.OrderSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.safeking.shop.domain.item.domain.entity.QItem.item;
import static com.safeking.shop.domain.order.domain.entity.QDelivery.delivery;
import static com.safeking.shop.domain.order.domain.entity.QOrder.order;
import static com.safeking.shop.domain.order.domain.entity.QOrderItem.orderItem;
import static com.safeking.shop.domain.payment.domain.entity.QSafeKingPayment.safeKingPayment;
import static com.safeking.shop.domain.user.domain.entity.member.QMember.member;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.springframework.util.StringUtils.*;

@Repository
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public OrderRepositoryImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Order> findOrders(Pageable pageable, OrderSearchCondition condition, Long memberId) {
        List<Order> content = queryFactory
                .selectFrom(order)
                .leftJoin(order.orderItems, orderItem).fetchJoin()
                .leftJoin(order.safeKingPayment, safeKingPayment).fetchJoin()
                .leftJoin(order.delivery, delivery).fetchJoin()
                .leftJoin(orderItem.item, item).fetchJoin()
                .where(
                        order.member.id.eq(memberId),
                        betweenDate(condition.getFromDate(), condition.getToDate()),
                        keywordContains(condition.getKeyword()),
                        deliveryStatusEq(condition.getDeliveryStatus()),
                        paymentStatusEq(condition.getPaymentStats())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(order.count())
                .from(order)
                .where(
                        order.member.id.eq(memberId),
                        betweenDate(condition.getFromDate(), condition.getToDate()),
                        keywordContains(condition.getKeyword()),
                        deliveryStatusEq(condition.getDeliveryStatus()),
                        paymentStatusEq(condition.getPaymentStats())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<Order> findOrdersByAdmin(Pageable pageable, OrderSearchCondition condition) {

        List<Order> content = queryFactory
                .selectFrom(order)
                .leftJoin(order.orderItems, orderItem).fetchJoin()
                .leftJoin(order.safeKingPayment, safeKingPayment).fetchJoin()
                .leftJoin(order.delivery, delivery).fetchJoin()
                .leftJoin(order.member, member).fetchJoin()
                .leftJoin(orderItem.item, item).fetchJoin()
                .where(
                        betweenDate(condition.getFromDate(), condition.getToDate()),
                        keywordContains(condition.getKeyword()),
                        deliveryStatusEq(condition.getDeliveryStatus()),
                        paymentStatusEq(condition.getPaymentStats())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(order.count())
                .from(order)
                .where(
                        betweenDate(condition.getFromDate(), condition.getToDate()),
                        keywordContains(condition.getKeyword()),
                        deliveryStatusEq(condition.getDeliveryStatus()),
                        paymentStatusEq(condition.getPaymentStats())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression paymentStatusEq(String paymentStats) {
        return hasText(paymentStats) ? order.safeKingPayment.status.eq(PaymentStatus.valueOf(paymentStats)) : null;
    }

    private BooleanExpression deliveryStatusEq(String deliveryStatus) {
        return hasText(deliveryStatus) ? order.delivery.status.eq(DeliveryStatus.valueOf(deliveryStatus)) : null;
    }

    private BooleanExpression keywordContains(String keyword) {
        return hasText(keyword) ? orderItem.item.name.containsIgnoreCase(keyword) : null;
    }

    private BooleanExpression betweenDate(String fromDate, String toDate) {

        //종료 주문일시만 있을 경우
        LocalDateTime from = null;
        LocalDateTime to = null;

        //검색 시작, 종료 주문일시 둘다 있을 경우
        if(hasText(fromDate) && hasText(toDate)) {
            from = LocalDate.parse(fromDate, ofPattern("yyyy-MM-dd")).atTime(0, 0, 0);
            to = LocalDate.parse(toDate, ofPattern("yyyy-MM-dd")).atTime(23, 59, 59);

            return order.createDate.between(from, to);
        }

        //시작 주문일시만 있을 경우
        if (hasText(fromDate)) {

            from = LocalDate.parse(fromDate, ofPattern("yyyy-MM-dd")).atTime(0, 0, 0);
            to = LocalDate.parse(fromDate, ofPattern("yyyy-MM-dd")).atTime(23, 59, 59);

            return order.createDate.between(from, to);
        }

        //종료 주문일시만 있을 경우
        if(hasText(toDate)) {

            from = LocalDate.parse(toDate, ofPattern("yyyy-MM-dd")).atTime(0, 0, 0);
            to = LocalDate.parse(toDate, ofPattern("yyyy-MM-dd")).atTime(23, 59, 59);

            return order.createDate.between(from, to);
        }

        LocalDateTime now = LocalDateTime.now();
        from = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0);
        to = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 23, 59, 59);

        return order.createDate.between(from, to);
    }
}

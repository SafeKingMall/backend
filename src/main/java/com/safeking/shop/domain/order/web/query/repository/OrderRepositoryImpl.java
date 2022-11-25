package com.safeking.shop.domain.order.web.query.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.safeking.shop.domain.order.domain.entity.Order;
import com.safeking.shop.domain.order.domain.entity.status.DeliveryStatus;
import com.safeking.shop.domain.order.domain.entity.status.PaymentStatus;
import com.safeking.shop.domain.order.web.query.dto.OrderSearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.safeking.shop.domain.item.domain.entity.QItem.item;
import static com.safeking.shop.domain.order.domain.entity.QDelivery.delivery;
import static com.safeking.shop.domain.order.domain.entity.QOrder.*;
import static com.safeking.shop.domain.order.domain.entity.QOrderItem.orderItem;
import static com.safeking.shop.domain.order.domain.entity.QPayment.payment;
import static java.time.LocalDateTime.*;
import static java.time.format.DateTimeFormatter.*;
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
    public Page<Order> findOrders(Pageable pageable, OrderSearchCondition condition) {
        List<Order> content = queryFactory
                .select(order)
                .from(order)
                .join(order.orderItems, orderItem).fetchJoin()
                .join(order.payment, payment).fetchJoin()
                .join(order.delivery, delivery).fetchJoin()
                .join(orderItem.item, item).fetchJoin()
                .where(
                        order.member.id.eq(condition.getMemberId()),
                        betweenDate(condition.getFromDate(), condition.getToDate()),
                        keywordContains(condition.getKeyword()),
                        deliveryStatusEq(condition.getDeliveryStatus()),
                        paymentStatusEq(condition.getPaymentStats())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory
                .select(order.count())
                .from(order)
                .where(
                        order.member.id.eq(condition.getMemberId()),
                        betweenDate(condition.getFromDate(), condition.getToDate()),
                        keywordContains(condition.getKeyword()),
                        deliveryStatusEq(condition.getDeliveryStatus()),
                        paymentStatusEq(condition.getPaymentStats())
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, count);
    }

    private BooleanExpression paymentStatusEq(String paymentStats) {
        return hasText(paymentStats) ? order.payment.status.eq(PaymentStatus.valueOf(paymentStats)) : null;
    }

    private BooleanExpression deliveryStatusEq(String deliveryStatus) {
        return hasText(deliveryStatus) ? order.delivery.status.eq(DeliveryStatus.valueOf(deliveryStatus)) : null;
    }

    private BooleanExpression keywordContains(String keyword) {
        return hasText(keyword) ? orderItem.item.name.contains(keyword) : null;
    }

    private BooleanExpression betweenDate(String fromDate, String toDate) {

        LocalDateTime from;
        LocalDateTime to;

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
        from = LocalDate.parse(toDate, ofPattern("yyyy-MM-dd")).atTime(0, 0, 0);
        to = LocalDate.parse(toDate, ofPattern("yyyy-MM-dd")).atTime(23, 59, 59);

        return order.createDate.between(from, to);
    }
}

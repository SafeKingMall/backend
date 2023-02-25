package com.safeking.shop.domain.order.web.query.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.safeking.shop.domain.exception.OrderException;
import com.safeking.shop.domain.order.domain.entity.Order;
import com.safeking.shop.domain.order.domain.entity.QOrderItem;
import com.safeking.shop.domain.order.domain.entity.status.DeliveryStatus;
import com.safeking.shop.domain.order.domain.entity.status.OrderStatus;
import com.safeking.shop.domain.order.web.query.repository.querydto.admin.orderlist.*;
import com.safeking.shop.domain.order.web.query.repository.querydto.user.orderlist.*;
import com.safeking.shop.domain.payment.domain.entity.PaymentStatus;
import com.safeking.shop.domain.order.web.dto.request.user.search.OrderSearchCondition;
import com.safeking.shop.domain.payment.web.client.dto.request.PaymentSearchCondition;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
     주문에서 결제, 배송, 주문상품을 조회해야함.
     * 주문상품은 일대다 관계이기 때문에 페이징이 불가하다.
     * where 절에 item.name 으로 검색하는 조건이 없으면 hibernate.default_batch_fetch_size를 이용하여 조회할수 있음
     * where 절에 item.name 검색 조건이 들어가야하기때문에
     * order조회와 orderItem조회를 분리함.
     */
    @Override
    public Page<UserOrderListQueryDto> findOrdersByUser(Pageable pageable, OrderSearchCondition condition, Long memberId) {

        List<UserOrderListQueryDto> content = queryFactory
                .select(new QUserOrderListQueryDto(
                                order.id,
                                order.status.stringValue(),
                                order.safeKingPayment.amount,
                                order.createDate,
                                order.merchantUid,
                                new QUserOrderListPaymentQueryDto(safekingPayment.status.stringValue(), safekingPayment.paidAt, safekingPayment.cancelledAt),
                                new QUserOrderListDeliveryQueryDto(delivery.status.stringValue())
                        )
                )
                .from(order)
                .leftJoin(order.safeKingPayment, safekingPayment)
                .leftJoin(order.delivery, delivery)
                .where(
                        order.member.id.eq(memberId),
                        orderBetweenDate(condition.getFromDate(), condition.getToDate()),
                        deliveryStatusEq(condition.getDeliveryStatus()),
                        safekingPayment.status.ne(PaymentStatus.CANCEL),
                        safekingPayment.status.ne(PaymentStatus.READY),
                        order.status.ne(OrderStatus.CANCEL),
                        order.status.ne(OrderStatus.READY)
                )
                .orderBy(order.createDate.desc())
                .fetch();

        // 주문 아이디 저장
        List<Long> orderIds = content.stream()
                .map(o -> o.getId())
                .collect(Collectors.toList());

        // 상품명 조건 조회 결과 Map으로 변환
        Map<Long, List<UserOrderListOrderItemQueryDto>> orderItemMap = findOrderItemMap(condition, orderIds);

        // 주문 조회 결과에 상품명 조건조회 결과 저장
        content.forEach(o -> o.setOrderItems(orderItemMap.get(o.getId())));

        // 주문 상품이 null이 아닌 주문 컬렉션으로 변경
        List<UserOrderListQueryDto> resultContent = content.stream()
                .filter(o -> o.getOrderItems() != null)
                .collect(Collectors.toList());

        return getCutomPageImpl(pageable, resultContent);
    }

    @NotNull
    private Map<Long, List<UserOrderListOrderItemQueryDto>> findOrderItemMap(OrderSearchCondition condition, List<Long> orderIds) {
        // 상품명 조건 조회
        List<UserOrderListOrderItemQueryDto> orderItemsContent = queryFactory
                .select(new QUserOrderListOrderItemQueryDto(orderItem.order.id, orderItem.id, orderItem.item.name, orderItem.item.fileName))
                .from(orderItem)
                .leftJoin(orderItem.item, item)
                .where(
                        orderItem.order.id.in(orderIds),
                        keywordContains(condition.getKeyword())
                )
                .fetch();

        // 상품명 조건 조회 결과 Map으로 변환
        Map<Long, List<UserOrderListOrderItemQueryDto>> orderItemMap = orderItemsContent.stream()
                .collect(Collectors.groupingBy(orderItemQueryDto -> orderItemQueryDto.getOrderId()));
        return orderItemMap;
    }

    /**
     * 주문에서 회원, 결제, 배송, 주문상품을 조회해야함.
     * 주문상품은 일대다 관계이기 때문에 페이징이 불가하다.
     * where 절에 item.name 으로 검색하는 조건이 없으면 hibernate.default_batch_fetch_size를 이용하여 조회할수 있음
     * where 절에 item.name 검색 조건이 들어가야하기때문에
     * order조회와 orderItem조회를 분리함.
     *
     * 1. Order(ToOne 관계) 조회
     * 2. 1에서 조회한 결과를 orderIds로 저장
     * 3. orderItem을 orderId IN 절을(2번의 orderIds 사용) 통해 한번에 조회(where 조건 포함)
     * 4. 3에서 조회한 결과(orderItem)를 1에 삽입
     * 5. 4의 결과에서 orderItem이 null인 경우 제외
     * 6. 5의 결과를 Page로 변환하고 반환
     */
    @Override
    public Page<AdminOrderListQueryDto> findOrdersByAdmin(Pageable pageable, OrderSearchCondition condition) {

        // 주문 전체 조회
        List<AdminOrderListQueryDto> content = queryFactory
                .select(new QAdminOrderListQueryDto(
                        order.id,
                        order.status.stringValue(),
                        order.safeKingPayment.amount,
                        order.createDate,
                        order.merchantUid,
                        new QAdminOrderListPaymentQueryDto(order.safeKingPayment.status.stringValue()),
                        new QAdminOrderListMemberQueryDto(order.member.name),
                        new QAdminOrderListDeliveryQueryDto(order.delivery.receiver, order.delivery.status.stringValue())
                        )
                )
                .from(order)
                .leftJoin(order.safeKingPayment, safekingPayment)
                .leftJoin(order.delivery, delivery)
                .leftJoin(order.member, member)
                .where(
                        orderBetweenDate(condition.getFromDate(), condition.getToDate()),
                        deliveryStatusEq(condition.getDeliveryStatus()),
                        paymentStatusEq(condition.getPaymentStatus())
                )
                .orderBy(order.createDate.desc())
                .fetch();

        // 주문 아이디 저장
        List<Long> orderIds = content.stream()
                .map(o -> o.getId())
                .collect(Collectors.toList());

        // 상품명으로 검색 조건
        Map<Long, List<AdminOrderListOrderItemQueryDto>> orderItemMap = findOrderItemMap(orderIds, condition.getKeyword());

        // 주문객체에 주문 상품컬렉션 저장
        content.forEach(o -> o.setOrderItems(orderItemMap.get(o.getId())));

        // 주문상품이 null이 아닌 컬렉션으로 구성
        List<AdminOrderListQueryDto> resultContent = content.stream()
                .filter(o -> o.getOrderItems() != null)
                .collect(Collectors.toList());

        // List를 Page로 변환
        return getCutomPageImpl(pageable, resultContent);
    }

    // List를 Page로 변환
    @NotNull
    private <T> PageImpl<T> getCutomPageImpl(Pageable pageable, List<T> resultContent) {

        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        int start = (int) pageRequest.getOffset();
        int end = Math.min(start + pageRequest.getPageSize(), resultContent.size());

        if(start > end) {
            throw new OrderException("데이터가 없습니다. 관리자에게 문의하세요.");
        }

        return new PageImpl<>(resultContent.subList(start, end), pageRequest, resultContent.size());
    }

    private Map<Long, List<AdminOrderListOrderItemQueryDto>> findOrderItemMap(List<Long> orderIds, String keyword) {
        // 주문 상품 검색(item.name 조건 포함)
        List<AdminOrderListOrderItemQueryDto> orderItems = queryFactory.select(new QAdminOrderListOrderItemQueryDto(orderItem.order.id, orderItem.id, orderItem.item.name))
                .from(orderItem)
                .leftJoin(orderItem.item, item)
                .where(
                        orderItem.order.id.in(orderIds),
                        keywordContains(keyword)
                )
                .fetch();

        // Map 으로 변환
        Map<Long, List<AdminOrderListOrderItemQueryDto>> orderItemMap = orderItems.stream()
                .collect(Collectors.groupingBy(orderItemQueryDto -> orderItemQueryDto.getOrderId()));

        return orderItemMap;
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
                        safekingPayment.status.eq(PaymentStatus.CANCEL)
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
                        safekingPayment.status.eq(PaymentStatus.CANCEL)
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    // 결제 상태 조건
    private BooleanExpression paymentStatusEq(String paymentStatus) {
        try {
            return hasText(paymentStatus) ? order.safeKingPayment.status.eq(PaymentStatus.valueOf(paymentStatus)) : null;
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

    /**
     * returns a view (not a new list) of the sourceList for the
     * range based on page and pageSize
     * @param sourceList
     * @param page, page number should start from 1
     * @param pageSize
     * @return
     * custom error can be given instead of returning emptyList
     */
    private static <T> List<T> getPage(List<T> sourceList, int page, int pageSize) {
        if(pageSize <= 0 || page <= 0) {
            throw new IllegalArgumentException("invalid page size: " + pageSize);
        }

        int fromIndex = (page - 1) * pageSize;
        if(sourceList == null || sourceList.size() <= fromIndex){
            return Collections.emptyList();
        }

        // toIndex exclusive
        return sourceList.subList(fromIndex, Math.min(fromIndex + pageSize, sourceList.size()));
    }
}

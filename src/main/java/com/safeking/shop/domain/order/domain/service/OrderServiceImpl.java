package com.safeking.shop.domain.order.domain.service;

import com.safeking.shop.domain.exception.OrderException;
import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.order.domain.entity.Delivery;
import com.safeking.shop.domain.order.domain.entity.Order;
import com.safeking.shop.domain.order.domain.entity.OrderItem;
import com.safeking.shop.domain.order.domain.entity.Payment;
import com.safeking.shop.domain.order.domain.repository.OrderRepository;
import com.safeking.shop.domain.order.web.OrderConst;
import com.safeking.shop.domain.order.web.dto.request.cancel.CancelRequest;
import com.safeking.shop.domain.order.web.dto.request.cancel.CancelOrderRequest;
import com.safeking.shop.domain.order.web.dto.request.order.OrderRequest;
import com.safeking.shop.domain.order.web.dto.request.modify.ModifyInfoRequest;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.safeking.shop.domain.order.web.OrderConst.*;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderServiceSubMethod orderServiceSubMethod;
    private final MemberRepository memberRepository;

    /**
     * 주문
     */
    @Override
    public Long order(Member member, OrderRequest orderRequest) {

        //상품 조회
        List<Item> items = orderServiceSubMethod.findItems(orderRequest.getOrderItemRequests());

        // 배송 정보 생성 및 저장
        Delivery delivery = orderServiceSubMethod.createDelivery(orderRequest);

        // 주문상품 생성 및 저장
        List<OrderItem> orderItems = orderServiceSubMethod.createOrderItems(orderRequest, items);

        // 결제

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderRequest.getMemo(), orderItems);
        orderRepository.save(order);

        return order.getId();
    }

    /**
     * 주문(배송) 정보 조회
     */
    @Override
    public Order findOrder(Long id) {
        Optional<Order> orderOptional = orderRepository.findOrder(id);
        Order findOrder = orderOptional.orElseThrow(() -> new OrderException(ORDER_FIND_FAIL));

        return findOrder;
    }

    /**
     * 주문 상세 조회
     */
    @Override
    public Order findOrderDetail(Long id) {
        Optional<Order> findOrderDetailOptional = orderRepository.findOrderDetail(id);
        Order findOrder = findOrderDetailOptional.orElseThrow(() -> new OrderException(ORDER_DETAIL_FIND_FAIL));

        return findOrder;
    }

    /**
     * 주문 취소
     */
    @Override
    public void cancel(CancelRequest cancelRequest) {

        cancelRequest.getOrders()
                .stream()
                .map(CancelOrderRequest::getId)
                .forEach((id) -> {
                    Optional<Order> findOrder = orderRepository.findById(id);
                    Order order = findOrder.orElseThrow(() -> new OrderException(ORDER_NONE));
                    order.cancel();
                });
    }

    /**
     * 주문 정보 수정
     */
    @Override
    public Long modifyOrder(ModifyInfoRequest modifyInfoRequest) {

        Optional<Order> findOrderOptional = orderRepository.findById(modifyInfoRequest.getOrder().getId());
        Order findOrder = findOrderOptional.orElseThrow(() -> new OrderException(ORDER_NONE));

        Delivery delivery = findOrder.getDelivery();

        delivery.changeDelivery(modifyInfoRequest.getDelivery().getReceiver(),
                modifyInfoRequest.getDelivery().getPhoneNumber(),
                modifyInfoRequest.getDelivery().getAddress(),
                modifyInfoRequest.getDelivery().getMemo());

        findOrder.changeMemo(modifyInfoRequest.getOrder().getMemo());

        return findOrder.getId();
    }
}

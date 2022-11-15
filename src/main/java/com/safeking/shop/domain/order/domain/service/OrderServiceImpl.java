package com.safeking.shop.domain.order.domain.service;

import com.safeking.shop.domain.exception.OrderException;
import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.order.domain.entity.Delivery;
import com.safeking.shop.domain.order.domain.entity.Order;
import com.safeking.shop.domain.order.domain.entity.OrderItem;
import com.safeking.shop.domain.order.domain.repository.OrderRepository;
import com.safeking.shop.domain.order.web.OrderConst;
import com.safeking.shop.domain.order.web.dto.request.cancel.CancelDto;
import com.safeking.shop.domain.order.web.dto.request.cancel.CancelOrderDtos;
import com.safeking.shop.domain.order.web.dto.request.order.OrderDto;
import com.safeking.shop.domain.order.web.dto.request.modify.ModifyInfoDto;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    public Long order(Member member, OrderDto orderDto) {

        //상품 조회
        List<Item> items = orderServiceSubMethod.findItems(orderDto.getOrderItemDtos());

        // 배송 정보 생성 및 저장
        Delivery delivery = orderServiceSubMethod.createDelivery(orderDto);

        // 주문상품 생성 및 저장
        List<OrderItem> orderItems = orderServiceSubMethod.createOrderItems(orderDto, items);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderDto.getMemo(), orderItems);
        orderRepository.save(order);

        return order.getId();
    }

    /**
     * 주문 취소
     */
    @Override
    public void cancel(CancelDto cancelDto) {

        cancelDto.getOrders()
                .stream()
                .map(CancelOrderDtos::getId)
                .forEach((id) -> {
                    Optional<Order> findOrder = orderRepository.findById(id);
                    Order order = findOrder.orElseThrow(() -> new OrderException(OrderConst.ORDER_NONE));
                    order.cancel();
                });
    }

    /**
     * 주문 정보 수정
     */
    @Override
    public Long modifyOrder(ModifyInfoDto modifyInfoDto) {

        Optional<Order> findOrderOptional = orderRepository.findById(modifyInfoDto.getOrder().getId());
        Order findOrder = findOrderOptional.orElseThrow(() -> new OrderException(OrderConst.ORDER_NONE));

        Delivery delivery = findOrder.getDelivery();

        delivery.changeDelivery(modifyInfoDto.getDelivery().getReceiver(),
                modifyInfoDto.getDelivery().getPhoneNumber(),
                modifyInfoDto.getDelivery().getAddress(),
                modifyInfoDto.getDelivery().getMemo());

        findOrder.changeMemo(modifyInfoDto.getOrder().getMemo());

        return findOrder.getId();
    }


    /**
     * 토큰 검증, 회원 검증
     */
    private Member findMember(String username) {
        Optional<Member> findMemberOptional = memberRepository.findByUsername(username);
        Member findMember = findMemberOptional.orElseThrow(() -> new OrderException("주문서비스 -> 회원이 없습니다."));

        return findMember;
    }
}

package com.safeking.shop.domain.order.domain.service;

import com.safeking.shop.domain.admin.domain.entity.Admin;
import com.safeking.shop.domain.exception.OrderException;
import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.item.domain.repository.ItemRepository;
import com.safeking.shop.domain.order.domain.entity.Delivery;
import com.safeking.shop.domain.order.domain.entity.Order;
import com.safeking.shop.domain.order.domain.entity.OrderItem;
import com.safeking.shop.domain.exception.ItemException;
import com.safeking.shop.domain.order.domain.entity.status.DeliveryStatus;
import com.safeking.shop.domain.order.domain.repository.DeliveryRepository;
import com.safeking.shop.domain.order.domain.repository.OrderItemRepository;
import com.safeking.shop.domain.order.domain.repository.OrderRepository;
import com.safeking.shop.domain.order.web.OrderConst;
import com.safeking.shop.domain.order.web.dto.request.cancel.CancelDto;
import com.safeking.shop.domain.order.web.dto.request.cancel.CancelOrderDtos;
import com.safeking.shop.domain.order.domain.service.dto.order.OrderItemDto;
import com.safeking.shop.domain.order.domain.service.login.LoginBehavior;
import com.safeking.shop.domain.order.domain.service.dto.order.OrderDto;
import com.safeking.shop.domain.order.web.dto.request.modify.ModifyInfoDto;
import com.safeking.shop.domain.user.domain.entity.Member;
import com.safeking.shop.domain.user.domain.entity.MemberAccountType;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import com.safeking.shop.domain.user.domain.repository.NormalAccountRepository;
import com.safeking.shop.domain.user.domain.repository.SocialAccountRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final NormalAccountRepository normalAccountRepository;
    private final SocialAccountRepository socialAccountRepository;
    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;
    private final ItemRepository itemRepository;
    private final OrderItemRepository orderItemRepository;
    private final MemberRepository memberRepository;
    private LoginBehavior loginBehavior;

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
     * 주문
     */
    @Override
    public Long order(OrderDto orderDto) {

        //상품 조회
        List<Item> items = findItems(orderDto.getOrderItemDtos());

        //회원 조회
        //==jwt 사용하여 조회==

        // 배송 정보 생성 및 저장
        Delivery delivery = createDelivery(orderDto);

        // 주문상품 생성 및 저장
        List<OrderItem> orderItems = createOrderItems(orderDto, items);

        // 주문 생성
        //Order order = Order.createOrder(new Member(), delivery, orderDto.getMemo(), orderItems);
        return null;
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
     * 배송 정보 생성 및 저장
     */
    private Delivery createDelivery(OrderDto orderDto) {
        //배송 정보 생성
        Delivery delivery = Delivery.createDelivery(orderDto.getReceiver(), orderDto.getPhoneNumber(),
                orderDto.getAddress(), DeliveryStatus.PREPARATION, orderDto.getOrderDeliveryDto().getMemo());
        //배송 정보 저장
        deliveryRepository.save(delivery);

        return delivery;
    }

    /**
     * 주문상품 생성 및 저장
     */
    private List<OrderItem> createOrderItems(OrderDto orderDto, List<Item> items) {

        List<OrderItem> orderItems = new ArrayList<>();

        //Client에서 받은 items(orderDto.getItemDtos())와 DB에서 조회한 items의 크기가 일치한지 확인
        if(items.size() != orderDto.getOrderItemDtos().size()) {
            throw new ItemException("해당 상품에 대한 정보가 없습니다. 관리자에게 문의 하세요.");
        }

        for(int i = 0; i < items.size(); i++) {
            //주문상품 생성
            OrderItem orderItem = OrderItem.createOrderItem(items.get(i),
                    items.get(i).getPrice(),
                    orderDto.getOrderItemDtos().get(i).getCount());
            //주문상품 저장
            orderItemRepository.save(orderItem);
            orderItems.add(orderItem);
        }

        return orderItems;
    }

    /**
     * 상품 조회
     */
    private List<Item> findItems(List<OrderItemDto> orderItemDtos) {

        Optional<Item> findItemsOptional;
        List<Item> items = new ArrayList<>();

        List<Long> itemIds = orderItemDtos.stream()
                .map(OrderItemDto::getId)
                .collect(Collectors.toList());

        for(Long id : itemIds) {
            findItemsOptional = itemRepository.findById(id);
            items.add(findItemsOptional.orElseThrow(() -> new ItemException("상품이 없습니다.")));
        }

        return items;
    }


    //로그인 방식 변경
    private void setLoginBehavior(LoginBehavior loginBehavior) {
        this.loginBehavior = loginBehavior;
    }

    private LoginBehavior getLoginBehavior() {
        return this.loginBehavior;
    }
}

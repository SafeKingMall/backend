package com.safeking.shop.domain.order.domain.service;

import com.safeking.shop.domain.exception.OrderException;
import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.item.domain.repository.ItemRepository;
import com.safeking.shop.domain.order.domain.entity.Delivery;
import com.safeking.shop.domain.order.domain.entity.Order;
import com.safeking.shop.domain.order.domain.entity.OrderItem;
import com.safeking.shop.domain.exception.ItemException;
import com.safeking.shop.domain.order.domain.repository.DeliveryRepository;
import com.safeking.shop.domain.order.domain.repository.OrderItemRepository;
import com.safeking.shop.domain.order.domain.repository.OrderRepository;
import com.safeking.shop.domain.order.domain.service.dto.DeliveryDto;
import com.safeking.shop.domain.order.domain.service.dto.OrderCancelDto;
import com.safeking.shop.domain.order.domain.service.login.LoginBehavior;
import com.safeking.shop.domain.order.domain.service.login.LoginDto;
import com.safeking.shop.domain.order.domain.service.dto.OrderDto;
import com.safeking.shop.domain.order.domain.service.login.NormalLogin;
import com.safeking.shop.domain.order.domain.service.login.SocialLogin;
import com.safeking.shop.domain.user.domain.repository.NormalAccountRepository;
import com.safeking.shop.domain.user.domain.repository.SocialAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
    private LoginBehavior loginBehavior;

    @Override
    public void cancel(OrderCancelDto orderCancelDto) {
        orderCancelDto.getIds()
                .forEach((id) -> {
                    Optional<Order> findOrder = orderRepository.findById(id);
                    Order order = findOrder.orElseThrow(() -> new OrderException("주문이 없습니다."));
                    order.cancel();
                });
    }

    @Override
    public Long order(OrderDto orderDto, DeliveryDto deliveryDto) {

        // 상품 조회
        Optional<Item> findItem = itemRepository.findById(orderDto.getItemId());
        Item item = findItem.orElseThrow(() -> new ItemException("상품이 없습니다."));

        // 배송 정보 생성
        Delivery delivery = Delivery.createDelivery(deliveryDto.getReceiver(),
                deliveryDto.getPhoneNumber(),
                deliveryDto.getAddress(),
                deliveryDto.getStatus(),
                deliveryDto.getShippingStartDate(),
                deliveryDto.getShippingEndDate());
        deliveryRepository.save(delivery);

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), orderDto.getCount());
        item.removeItemQuantity(orderDto.getCount());

        // 회원 조회
        if(orderDto.getUserId() != null) {
            setLoginBehavior(new NormalLogin(normalAccountRepository, orderDto.getUserId()));
        } else {
            setLoginBehavior(new SocialLogin(socialAccountRepository, orderDto.getOauthId(), orderDto.getProvider()));
        }

        Object loginMember = loginBehavior.login();
        // 주문 생성

        return null;
    }

    @Override
    public Long updateOrder(OrderDto orderOrderDto, DeliveryDto deliveryCreateDto) {
        return null;
    }

    //로그인 방식 변경
    private void setLoginBehavior(LoginBehavior loginBehavior) {
        this.loginBehavior = loginBehavior;
    }

    private LoginBehavior getLoginBehavior() {
        return this.loginBehavior;
    }
}

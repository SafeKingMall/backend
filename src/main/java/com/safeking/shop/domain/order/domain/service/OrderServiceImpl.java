package com.safeking.shop.domain.order.domain.service;

import com.safeking.shop.domain.order.domain.entity.Delivery;
import com.safeking.shop.domain.order.domain.entity.Order;
import com.safeking.shop.domain.order.domain.entity.OrderItem;
import com.safeking.shop.domain.order.domain.repository.DeliveryRepository;
import com.safeking.shop.domain.order.domain.repository.OrderRepository;
import com.safeking.shop.domain.order.domain.service.dto.DeliveryCreateDto;
import com.safeking.shop.domain.order.domain.service.dto.OrderOrderDto;
import com.safeking.shop.domain.user.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;

    @Override
    public void cancel(List<Long> ids) {

    }

    @Override
    public Long order(OrderOrderDto ooDto, DeliveryCreateDto dcDto) {

        // 회원 조회
        // 상품 조회

        // 배송 정보 생성
        Delivery delivery = Delivery.createDelivery(dcDto.getReceiver(),
                dcDto.getPhoneNumber(),
                dcDto.getAddress(),
                dcDto.getStatus(),
                dcDto.getShippingStartDate(),
                dcDto.getShippingEndDate());
        deliveryRepository.save(delivery);

        // 주문상품 생성

        // 주문 생성

        return null;
    }

    @Override
    public Long update(OrderOrderDto orderOrderDto, DeliveryCreateDto deliveryCreateDto) {
        return null;
    }
}

package com.safeking.shop.domain.order.domain.service;

import com.safeking.shop.domain.order.domain.entity.Delivery;
import com.safeking.shop.domain.order.domain.repository.DeliveryRepository;
import com.safeking.shop.domain.order.domain.repository.OrderRepository;
import com.safeking.shop.domain.order.domain.service.dto.DeliveryCreateDto;
import com.safeking.shop.domain.order.domain.service.login.LoginBehavior;
import com.safeking.shop.domain.order.domain.service.login.LoginDto;
import com.safeking.shop.domain.order.domain.service.dto.OrderOrderDto;
import com.safeking.shop.domain.order.domain.service.login.NormalLogin;
import com.safeking.shop.domain.order.domain.service.login.SocialLogin;
import com.safeking.shop.domain.user.domain.entity.NormalAccount;
import com.safeking.shop.domain.user.domain.entity.SocialAccount;
import com.safeking.shop.domain.user.domain.repository.NormalAccountRepository;
import com.safeking.shop.domain.user.domain.repository.SocialAccountRepository;
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
    private final DeliveryRepository deliveryRepository;
    private final NormalAccountRepository normalAccountRepository;
    private final SocialAccountRepository socialAccountRepository;
    private LoginBehavior loginBehavior;

    @Override
    public void cancel(List<Long> ids) {

    }

    @Override
    public Long order(OrderOrderDto ooDto, DeliveryCreateDto dcDto) {

        // 회원 조회
        if(ooDto.getUserId() != null) {
            setLoginBehavior(new NormalLogin(normalAccountRepository, ooDto.getUserId()));
        } else {
            setLoginBehavior(new SocialLogin(socialAccountRepository, ooDto.getOauthId(), ooDto.getProvider()));
        }
        LoginDto login = loginBehavior.login();
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
    public Long updateOrder(OrderOrderDto orderOrderDto, DeliveryCreateDto deliveryCreateDto) {
        return null;
    }

    //로그인 방식 변경
    void setLoginBehavior(LoginBehavior loginBehavior) {
        this.loginBehavior = loginBehavior;
    }
}

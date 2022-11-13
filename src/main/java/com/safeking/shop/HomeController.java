package com.safeking.shop;

import com.safeking.shop.domain.admin.domain.entity.Admin;
import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.order.domain.entity.Delivery;
import com.safeking.shop.domain.order.domain.entity.Order;
import com.safeking.shop.domain.order.domain.entity.OrderItem;
import com.safeking.shop.domain.order.domain.entity.status.DeliveryStatus;
import com.safeking.shop.domain.order.domain.repository.DeliveryRepository;
import com.safeking.shop.domain.order.domain.repository.OrderRepository;
import com.safeking.shop.domain.order.domain.service.OrderService;
import com.safeking.shop.domain.user.domain.entity.Member;
import com.safeking.shop.domain.user.domain.entity.MemberAccountType;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HomeController {
    private final DeliveryRepository deliveryRepository;

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/add")
    @Transactional
    public Long add() {

        Delivery delivery = Delivery.createDelivery("아이유",
                "01012341234",
                "서울시 여의도",
                DeliveryStatus.PREPARATION,
                "문앞에 놓아주세요.");
        Delivery saveDelivery = deliveryRepository.save(delivery);

        return saveDelivery.getId();
    }
}

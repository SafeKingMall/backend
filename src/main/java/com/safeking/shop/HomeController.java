package com.safeking.shop;


import com.safeking.shop.domain.order.domain.entity.Delivery;
import com.safeking.shop.domain.order.domain.entity.status.DeliveryStatus;
import com.safeking.shop.domain.order.domain.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HomeController {
    private final DeliveryRepository deliveryRepository;

    @GetMapping("/api/v1/home")
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
                "문앞에 놓아주세요.",
                "dlwlrma@kakao.com");
        Delivery saveDelivery = deliveryRepository.save(delivery);

        return saveDelivery.getId();
    }
}

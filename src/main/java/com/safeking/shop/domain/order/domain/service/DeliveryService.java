package com.safeking.shop.domain.order.domain.service;

import com.safeking.shop.domain.order.domain.entity.Delivery;

public interface DeliveryService {
    //Delivery findDelivery(Long deliveryId); //배송 조회
    Long create(); //배송 정보 생성
    Long update(); //배송 정보 수정
}
